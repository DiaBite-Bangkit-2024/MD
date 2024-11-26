package com.capstone.diabite.ui.settings

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import com.capstone.diabite.R
import com.capstone.diabite.databinding.ActivityProfileBinding
import com.capstone.diabite.db.DataResult
import com.capstone.diabite.ui.dashboard.DashboardViewModel
import com.capstone.diabite.ui.login.LoginViewModel
import com.capstone.diabite.view.auth.AuthViewModelFactory

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private val profileVM: DashboardViewModel by viewModels()
    private val loginVM by viewModels<LoginViewModel> {
        AuthViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        loginVM.getSession().observe(this) { user ->
            if (user.isLogin) {
                profileVM.fetchUserProfile(user.token)
            }
        }
        binding.backButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.editIcon.setOnClickListener {

        }
        setupGenderSpinner()
        setupUserProf()


        binding.btnSaveChanges.setOnClickListener {
            saveUserData()
        }


    }

    private fun setupGenderSpinner() {
        val genderSpinner: Spinner = findViewById(R.id.spinner_gender)
        val genderAdapter = ArrayAdapter.createFromResource(
            this, R.array.gender_options, android.R.layout.simple_spinner_item
        )
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        genderSpinner.adapter = genderAdapter

        genderSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                if (position == 0) {
                    Toast.makeText(
                        this@ProfileActivity,
                        "Please select your gender",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }
    }

    private fun setupUserProf() {
        profileVM.userProfile.observe(this) { result ->
            when (result) {
                is DataResult.Loading -> {
                    // Show a loading indicator if needed
                }

                is DataResult.Success -> {
                    val data = result.data.profile
                    Log.d("ProfileActivity", "Fetched profile data: $data")
                    binding.apply {
                        etName.setText("${data.name}")
                        etEmail.setText("${data.email}")
                        etAge.setText("${data.age}")
                        etHeight.setText(data.height.toString())
                        spinnerGender.setSelection(
                            if (data.gender == "male") 1 else 2
                        )
                        etWeight.setText("${data.weight}")
                        etSystolic.setText("${data.systolic}")
                        etDiastolic.setText("${data.diastolic}")
                    }
                }


                is DataResult.Error -> {
                    Toast.makeText(
                        this,
                        "Failed to fetch profile: ${result.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun loadUserData() {
        val sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

        binding.etName.setText(sharedPreferences.getString("name", ""))
        binding.etEmail.setText(sharedPreferences.getString("email", ""))
        binding.etAge.setText(sharedPreferences.getInt("age", 0).toString())
        binding.etHeight.setText(sharedPreferences.getFloat("height", 0f).toString())
        binding.etWeight.setText(sharedPreferences.getFloat("weight", 0f).toString())

        val bloodPressure = sharedPreferences.getString("blood_pressure", "0/0") ?: "0/0"
        val parts = bloodPressure.split("/")
        if (parts.size == 2) {
            binding.etSystolic.setText(parts[0])
            binding.etDiastolic.setText(parts[1])
        }

        val gender = sharedPreferences.getString("gender", "")
        val genderPosition = resources.getStringArray(R.array.gender_options).indexOf(gender)
        if (genderPosition >= 0) {
            binding.spinnerGender.setSelection(genderPosition)
        }
    }

    private fun saveUserData() {
        val sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.putString("name", binding.etName.text.toString())
        editor.putString("email", binding.etEmail.text.toString())
        editor.putInt("age", binding.etAge.text.toString().toIntOrNull() ?: 0)
        editor.putFloat("height", binding.etHeight.text.toString().toFloatOrNull() ?: 0f)
        editor.putFloat("weight", binding.etWeight.text.toString().toFloatOrNull() ?: 0f)
        editor.putString(
            "blood_pressure",
            "${binding.etSystolic.text}/${binding.etDiastolic.text}"
        )
        editor.putString(
            "gender",
            binding.spinnerGender.selectedItem?.toString() ?: ""
        )

        editor.apply()

        Toast.makeText(this, "Changes saved!", Toast.LENGTH_SHORT).show()

        finish()
    }
}