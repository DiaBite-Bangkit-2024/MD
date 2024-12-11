package com.capstone.diabite.ui.settings.profile

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.capstone.diabite.R
import com.capstone.diabite.databinding.ActivityProfileBinding
import com.capstone.diabite.db.DataResult
import com.capstone.diabite.ui.dashboard.DashboardViewModel
import com.capstone.diabite.db.responses.UpdateProfileRequest
import com.capstone.diabite.db.pref.UserRepository
import com.capstone.diabite.ui.login.LoginViewModel
import com.capstone.diabite.view.auth.AuthViewModelFactory
import com.capstone.diabite.db.pref.UserPreference
import com.capstone.diabite.db.pref.dataStore
import com.yalantis.ucrop.UCrop
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private val profileVM: DashboardViewModel by viewModels()
    private val loginVM: LoginViewModel by viewModels {
        AuthViewModelFactory(
            UserRepository.getInstance(
                UserPreference.getInstance(dataStore)
            )
        )
    }
    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            result.data?.data?.let { uri ->
                startCrop(this, uri)
            } ?: showToast(getString(R.string.empty_image_warning))
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        loginVM.currentImageUri.value?.let { uri ->
            outState.putString("currentImageUri", uri.toString())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        savedInstanceState?.getString("currentImageUri")?.let { uriString ->
            loginVM.setImageUri(Uri.parse(uriString))
        }

        if (profileVM.userProfile.value !is DataResult.Success) {
            loginVM.getSession().observe(this) { user ->
                if (user.isLogin) {
                    profileVM.fetchUserProfile(user.token)
                }
            }
        }

        setupGenderSpinner()
        binding.backButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.editIcon.setOnClickListener {
            startGallery()
        }

        binding.btnSaveChanges.setOnClickListener {
            saveProfile()
        }

        loginVM.currentImageUri.observe(this) { uri ->
            if (uri != null) {
                Glide.with(this)
                    .load(uri)
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .fallback(R.drawable.person)
                    .placeholder(R.drawable.person)
                    .into(binding.profileImage)
            } else {
                Glide.with(this)
                    .load((profileVM.userProfile.value as? DataResult.Success)?.data?.profile?.avatar)
                    .fallback(R.drawable.person)
                    .placeholder(R.drawable.person)
                    .into(binding.profileImage)
            }
        }
        setupUserProf()
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == UCrop.REQUEST_CROP && resultCode == Activity.RESULT_OK) {
            val resultUri: Uri? = UCrop.getOutput(data!!)
            if (resultUri != null) {
                loginVM.setImageUri(resultUri)

                Glide.with(this)
                    .load(resultUri)
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .fallback(R.drawable.person)
                    .placeholder(R.drawable.person)
                    .into(binding.profileImage)
            } else {
                showToast("Failed to retrieve cropped image.")
            }
        } else if (resultCode == UCrop.RESULT_ERROR) {
            val cropError = UCrop.getError(data!!)
            showToast("Error occurred during cropping: ${cropError?.message}")
        }
    }


    private fun setupGenderSpinner() {
        val genderSpinner: Spinner = findViewById(R.id.spinner_gender)
        val genderAdapter = ArrayAdapter.createFromResource(
            this, R.array.gender_options, android.R.layout.simple_spinner_item
        )
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        genderSpinner.adapter = genderAdapter
    }

    private fun startGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        launcherIntentGallery.launch(intent)
    }

    private fun startCrop(context: Activity, imageUri: Uri) {
        val fileName = "${System.currentTimeMillis()}_${generateRandomString(10)}.jpg"
        val newDestinationFile = File(cacheDir, fileName)

        if (!newDestinationFile.exists()) newDestinationFile.createNewFile()
        val destinationUri = Uri.fromFile(newDestinationFile)

        val options = UCrop.Options().apply {
            setToolbarColor(ContextCompat.getColor(context, R.color.dark_background))
            setStatusBarColor(ContextCompat.getColor(context, R.color.dark_background))
            setActiveControlsWidgetColor(ContextCompat.getColor(context, R.color.dark_accent))
            setToolbarWidgetColor(ContextCompat.getColor(context, R.color.dark_accent))
            setCompressionQuality(80)
        }

        UCrop.of(imageUri, destinationUri)
            .withOptions(options)
            .withAspectRatio(1f, 1f)
            .withMaxResultSize(800, 800)
            .start(context)
    }

    private fun generateRandomString(length: Int): String {
        val chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
        return (1..length)
            .map { chars.random() }
            .joinToString("")
    }

    private fun saveProfile() {
        val avatarUri = loginVM.currentImageUri.value
        var avatarPart: MultipartBody.Part? = null

        if (avatarUri != null) {
            try {
                val avatarFile = File(avatarUri.path)
                val requestFile = avatarFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
                avatarPart =
                    MultipartBody.Part.createFormData("avatar", avatarFile.name, requestFile)
            } catch (e: Exception) {
                showToast("Failed to process avatar: ${e.message}")
                Log.e("SaveProfile", "Error processing avatar file: ${e.message}")
                return
            }
        }

        try {
            val updateRequest = UpdateProfileRequest(
                name = binding.etName.text.toString(),
                age = binding.etAge.text.toString().toInt(),
                gender = binding.spinnerGender.selectedItem.toString(),
                height = binding.etHeight.text.toString().toInt(),
                weight = binding.etWeight.text.toString().toInt(),
                systolic = binding.etSystolic.text.toString().toInt(),
                diastolic = binding.etDiastolic.text.toString().toInt(),
                avatar = avatarPart
            )

            loginVM.updateUserProfile(updateRequest)
            showToast("Profile updated successfully")

            Handler(Looper.getMainLooper()).postDelayed({
                finish()
            }, 1000)

        } catch (e: Exception) {
            showToast("Failed to save profile: ${e.message}")
            Log.e("SaveProfile", "Error: ${e.message}")
        }
    }


    private fun setupUserProf() {
        profileVM.userProfile.observe(this) { result ->
            when (result) {
                is DataResult.Loading -> {}

                is DataResult.Success -> {
                    val data = result.data.profile
                    val genderOptions = resources.getStringArray(R.array.gender_options)
                    val genderIndex = genderOptions.indexOfFirst { it.equals(data.gender, ignoreCase = true) }
                    Log.d("ProfileActivity", "Fetched profile data: $data")
                    binding.apply {
                        etName.setText(data.name)
                        etAge.setText("${data.age}")
                        etHeight.setText(data.height.toString())
                        spinnerGender.setSelection(if (genderIndex != -1) genderIndex else 0)
                        etWeight.setText("${data.weight}")
                        etSystolic.setText("${data.systolic}")
                        etDiastolic.setText("${data.diastolic}")
                        if (loginVM.currentImageUri.value == null && !data.avatar.isNullOrEmpty()) {
                            Glide.with(this@ProfileActivity)
                                .load(data.avatar)
                                .fallback(R.drawable.person)
                                .placeholder(R.drawable.person)
                                .into(profileImage)
                        } else if (loginVM.currentImageUri.value == null) {
                            profileImage.setImageResource(R.drawable.person)
                        }
                    }
                }

                is DataResult.Error -> {
                    Toast.makeText(this, "Failed to fetch profile: ${result.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}