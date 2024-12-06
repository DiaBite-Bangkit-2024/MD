package com.capstone.diabite.view

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.capstone.diabite.R
import com.capstone.diabite.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
        private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
       super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.btnClose.setOnClickListener { onBackPressedDispatcher.onBackPressed() }
    }
}