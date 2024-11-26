package com.capstone.diabite.ui.settings.profile

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.capstone.diabite.R
import com.capstone.diabite.databinding.ActivityProfileBinding
import com.capstone.diabite.db.DataResult
import com.capstone.diabite.db.UpdateProfileRequest
import com.capstone.diabite.db.pref.UserRepository
import com.capstone.diabite.ui.login.LoginViewModel
import com.capstone.diabite.view.auth.AuthViewModelFactory
import com.capstone.diabite.db.pref.UserPreference
import com.capstone.diabite.db.pref.dataStore

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
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

        binding.backButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.editIcon.setOnClickListener {

        }


//        binding.backButton.setOnTouchListener { v, event ->
//            when (event.action) {
//                MotionEvent.ACTION_DOWN -> {
//                    v.scaleX = 0.9f
//                    v.scaleY = 0.9f
//                }
//                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
//                    v.scaleX = 1f
//                    v.scaleY = 1f
//                }
//            }
//            false
//        }

        setupGenderSpinner()


        loginVM.userProfile.observe(this) { result ->
            when (result) {
                is DataResult.Loading -> {}
                is DataResult.Success -> {
                    val user = result.data
                    binding.etName.setText(user.name)
                    binding.etEmail.setText(user.newEmail)
                    binding.etAge.setText(user.age.toString())
                    binding.spinnerGender.setSelection(if (user.gender == "male") 1 else 2)
                    binding.etHeight.setText(user.height.toString())
                    binding.etWeight.setText(user.weight.toString())
                    binding.etSystolic.setText(user.systolic.toString())
                    binding.etDiastolic.setText(user.diastolic.toString())
                }
                is DataResult.Error -> {
                    Toast.makeText(this, result.message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        loginVM.fetchUserProfile()

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
}