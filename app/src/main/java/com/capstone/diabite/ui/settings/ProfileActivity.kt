package com.capstone.diabite.ui.settings

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.capstone.diabite.R
import com.capstone.diabite.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding

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

        binding.btnSaveChanges.setOnClickListener {
            saveUserData()
        }

        loadUserData()

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