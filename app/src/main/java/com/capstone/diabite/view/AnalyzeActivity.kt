package com.capstone.diabite.view

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.capstone.diabite.R
import com.capstone.diabite.databinding.ActivityAnalyzeBinding
import com.capstone.diabite.db.DataResult
import com.capstone.diabite.db.local.HistoryViewModel
import com.capstone.diabite.db.prediction.PredictionRequest
import com.capstone.diabite.db.prediction.PredictionViewModel
import com.capstone.diabite.db.pref.UserPreference
import com.capstone.diabite.db.pref.UserRepository
import com.capstone.diabite.db.pref.dataStore
import com.capstone.diabite.ui.dashboard.DashboardViewModel
import com.capstone.diabite.ui.login.LoginViewModel
import com.capstone.diabite.view.auth.AuthViewModelFactory
import kotlinx.coroutines.launch

class AnalyzeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAnalyzeBinding
    private val profileVM: DashboardViewModel by viewModels()
    private val predictionVM: PredictionViewModel by viewModels()
    private val historyVM: HistoryViewModel by viewModels()
    private val loginVM: LoginViewModel by viewModels {
        AuthViewModelFactory(
            UserRepository.getInstance(
                UserPreference.getInstance(dataStore)
            )
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAnalyzeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        fun convertAgeToCategory(age: Int): Int {
            return when (age) {
                in 18..24 -> 1
                in 25..29 -> 2
                in 30..34 -> 3
                in 35..39 -> 4
                in 40..44 -> 5
                in 45..49 -> 6
                in 50..54 -> 7
                in 55..59 -> 8
                in 60..64 -> 9
                in 65..69 -> 10
                in 70..74 -> 11
                in 75..79 -> 12
                else -> if (age >= 80) 13 else 0
            }
        }

        loginVM.getSession().observe(this) { user ->
            if (user.isLogin) {
                val token = user.token
                profileVM.fetchUserProfile(token)
                binding.btnSaveChanges.setOnClickListener {
                    val age = binding.etAge.text.toString()
                    val bmi = binding.etBmi.text.toString()
                    val generalHealth = binding.slideName.value.toInt()
                    val physicalHealth = binding.slidePhysical.value.toInt()
                    val mentalHealth = binding.slideMental.value.toInt()
                    val bloodPressure = if (binding.spinnerBlood.selectedItem.toString() == "Yes") 1 else 0
                    val cholesterol = if (binding.spinnerCholesterol.selectedItem.toString() == "Yes") 1 else 0
                    val stroke = if (binding.spinnerStroke.selectedItem.toString() == "Yes") 1 else 0
                    val heart = if (binding.spinnerHeart.selectedItem.toString() == "Yes") 1 else 0
                    val walking = if (binding.spinnerWalking.selectedItem.toString() == "Yes") 1 else 0

                    if (age.isNotBlank() && bmi.isNotBlank()) {

                        val ageCategory = convertAgeToCategory(age.toInt())
                        val inputList = listOf(
                            bloodPressure,
                            bmi.toFloat(),
                            generalHealth,
                            walking,
                            cholesterol,
                            ageCategory,
                            heart,
                            physicalHealth,
                            stroke,
                            mentalHealth
                        )

                        val request = PredictionRequest(inputList)

                        lifecycleScope.launch {
                            try {
                                predictionVM.postAnalyzeData(token, request)
                            } catch (e: Exception) {
                                Log.e("AnalyzeActivity", "Error posting data to prediction model: ${e.message}")
                            }
                        }

                        observePredictionResponse()
                    } else {
                        Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        binding.backButton.setOnClickListener {
            finish()
        }

        val yesNoOptions = arrayOf("Yes", "No")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, yesNoOptions).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        binding.apply {
            spinnerBlood.adapter = adapter
            spinnerCholesterol.adapter = adapter
            spinnerStroke.adapter = adapter
            spinnerHeart.adapter = adapter
            spinnerWalking.adapter = adapter
        }

        setupUserProf()

//        binding.slideName.addOnChangeListener { _, value, _ ->
//            Toast.makeText(this, "General Health: $value", Toast.LENGTH_SHORT).show()
//        }
//
//        binding.slidePhysical.addOnChangeListener { _, value, _ ->
//            Toast.makeText(this, "Physical Health (days unwell): $value", Toast.LENGTH_SHORT).show()
//        }
//
//        binding.slideMental.addOnChangeListener { _, value, _ ->
//            Toast.makeText(this, "Mental Health (days unwell): $value", Toast.LENGTH_SHORT).show()
//        }
    }

    private fun observePredictionResponse() {
        predictionVM.response.observe(this) { response ->
            if (response.error) {
                Toast.makeText(this, response.message, Toast.LENGTH_SHORT).show()
            } else {
                val prediction = (response.prediction * 100).toInt()
                val resultText = when {
                    prediction >= 70 -> "High Risk of Diabetes"
                    prediction <= 40 -> "Low Risk of Diabetes"
                    else -> "Moderate Risk of Diabetes"
                }
                historyVM.addHistory(prediction, resultText)
                showResultDialog(prediction, resultText)
                Toast.makeText(this, "Analysis completed", Toast.LENGTH_SHORT).show()
            }
        }

        predictionVM.errorMessage.observe(this) { error ->
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
        }
    }

    private fun showResultDialog(prediction: Int, summary: String) {
        val dialogView = layoutInflater.inflate(R.layout.popup_dialog, null)
        val dialog = android.app.AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        val circularProgressView = dialogView.findViewById<CircularProgressView>(R.id.circularProgressView)
        val progressText = dialogView.findViewById<TextView>(R.id.progressText)
        val resultText = dialogView.findViewById<TextView>(R.id.tv_result)

        circularProgressView.setProgress(prediction)
        progressText.text = "$prediction%"
        resultText.text = summary

        dialogView.findViewById<ImageButton>(R.id.close_button).setOnClickListener {
            dialog.dismiss()
        }

        dialogView.findViewById<TextView>(R.id.okay_text).setOnClickListener {
            Toast.makeText(this, "Okay clicked", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }

        dialogView.findViewById<TextView>(R.id.cancel_text).setOnClickListener {
            Toast.makeText(this, "Cancel clicked", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun setupUserProf() {
        profileVM.userProfile.observe(this) { result ->
            when (result) {
                is DataResult.Loading -> {}
                is DataResult.Success -> {
                    val data = result.data.profile
                    Log.d("AnalyzeActivity", "Fetched profile data: $data")
                    binding.apply {
                        etAge.setText("${data.age}")

                        val weight = data.weight
                        val height = data.height

                        if (weight > 0 && height > 0) {
                            val bmi = calculateBMI(weight, height)
                            etBmi.setText(String.format("%.2f", bmi))
                        } else {
                            etBmi.setText("Invalid data")
                        }
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

    private fun calculateBMI(weight: Int, height: Int): Float {
        val heightInMeters = height / 100.0
        return if (heightInMeters > 0) {
            (weight / (heightInMeters * heightInMeters)).toFloat()
        } else {
            0f
        }
    }
}