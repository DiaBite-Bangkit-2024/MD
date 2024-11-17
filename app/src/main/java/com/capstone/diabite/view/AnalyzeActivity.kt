package com.capstone.diabite.view

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.capstone.diabite.databinding.ActivityAnalyzeBinding

class AnalyzeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAnalyzeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityAnalyzeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

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

        binding.slideName.addOnChangeListener { _, value, _ ->
            Toast.makeText(this, "General Health: $value", Toast.LENGTH_SHORT).show()
        }

        binding.slidePhysical.addOnChangeListener { _, value, _ ->
            Toast.makeText(this, "Physical Health (days unwell): $value", Toast.LENGTH_SHORT).show()
        }

        binding.slideMental.addOnChangeListener { _, value, _ ->
            Toast.makeText(this, "Mental Health (days unwell): $value", Toast.LENGTH_SHORT).show()
        }

        binding.btnSaveChanges.setOnClickListener {
            val generalHealth = binding.slideName.value.toInt()
            val physicalHealth = binding.slidePhysical.value.toInt()
            val mentalHealth = binding.slideMental.value.toInt()
            val bloodPressure = binding.spinnerBlood.selectedItem.toString()
            val cholesterol = binding.spinnerCholesterol.selectedItem.toString()
            val stroke = binding.spinnerStroke.selectedItem.toString()
            val heart = binding.spinnerHeart.selectedItem.toString()
            val walking = binding.spinnerWalking.selectedItem.toString()

            Toast.makeText(
                this,
                "Data saved:\nGeneral Health: $generalHealth\nPhysical Days: $physicalHealth\nMental Days: $mentalHealth\nBlood Pressure: $bloodPressure\nCholesterol: $cholesterol\nStroke: $stroke\nHeart Disease: $heart\nWalking Difficulty: $walking",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}
