package com.capstone.diabite.ui.settings.profile

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
import com.capstone.diabite.db.UpdateProfileRequest
import com.capstone.diabite.db.pref.UserRepository
import com.capstone.diabite.ui.login.LoginViewModel
import com.capstone.diabite.view.auth.AuthViewModelFactory
import com.capstone.diabite.db.pref.UserPreference
import com.capstone.diabite.db.pref.dataStore

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
            val updateRequest = UpdateProfileRequest(
                name = binding.etName.text.toString(),
                newEmail = binding.etEmail.text.toString(),
                password = binding.etPassword.text.toString(),
                age = binding.etAge.text.toString().toInt(),
                gender = binding.spinnerGender.selectedItem.toString(),
                height = binding.etHeight.text.toString().toInt(),
                weight = binding.etWeight.text.toString().toInt(),
                systolic = binding.etSystolic.text.toString().toInt(),
                diastolic = binding.etDiastolic.text.toString().toInt()
            )
            loginVM.updateUserProfile(updateRequest)
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
}