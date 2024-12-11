package com.capstone.diabite.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.capstone.diabite.databinding.ActivityInitInfoBinding
import com.capstone.diabite.db.ApiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class InitInfoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInitInfoBinding
    private var name: String = ""
    private var email: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInitInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.backButton.setOnClickListener {
            val intent = Intent(this@InitInfoActivity, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }

        val genderOptions = listOf("Male", "Female", "Other")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, genderOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerGender.adapter = adapter

        email = intent.getStringExtra("email").orEmpty()
        name = intent.getStringExtra("name").orEmpty()

        binding.btnSaveChanges.setOnClickListener {
            val age = binding.etName.text.toString()
            val gender = binding.spinnerGender.selectedItem.toString()
            val height = binding.etHeight.text.toString()
            val weight = binding.etWeight.text.toString()
            val systolic = binding.etSystolic.text.toString()
            val diastolic = binding.etDiastolic.text.toString()

            Log.d("InitInfoActivity", "Input values: age=$age, gender=$gender, height=$height, weight=$weight, systolic=$systolic, diastolic=$diastolic")

            if (age.isEmpty() || gender.isEmpty() || height.isEmpty() || weight.isEmpty() || systolic.isEmpty() || diastolic.isEmpty()) {
                Toast.makeText(
                    this@InitInfoActivity,
                    "All fields must be filled",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                saveProfile(
                    email = email,
                    age = age.toInt(),
                    gender = gender,
                    height = height.toInt(),
                    weight = weight.toInt(),
                    systolic = systolic.toInt(),
                    diastolic = diastolic.toInt()
                )
            }
        }
    }

    private fun saveProfile(email: String, age: Int, gender: String, weight: Int, height: Int, systolic: Int, diastolic: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = ApiClient.getApiService2().saveProfile(email, age, gender, weight, height, systolic, diastolic)

                withContext(Dispatchers.Main) {
                    if (!response.error) {
                        Log.d("InitInfoActivity", "Profile saved successfully!")
                        Toast.makeText(this@InitInfoActivity, "Profile saved successfully!", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@InitInfoActivity, MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        intent.putExtra("name", name)
                        intent.putExtra("age", age)
                        intent.putExtra("height", height)
                        intent.putExtra("weight", weight)
                        intent.putExtra("systolic", systolic)
                        intent.putExtra("diastolic", diastolic)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this@InitInfoActivity, response.message, Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                Log.e("InitInfoActivity", "Error saving profile", e)
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@InitInfoActivity, "Failed to save profile: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}