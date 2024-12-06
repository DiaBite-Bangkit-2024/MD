package com.capstone.diabite.view.food

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.capstone.diabite.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.btnClose.setOnClickListener {
            onBackPressed()
        }

        val foodDetails = intent.getStringArrayListExtra("FOOD_DETAILS") ?: return

        binding.apply {
            foodName.text = foodDetails[0]
            calNum.text = foodDetails[1]
            protein.text = foodDetails[2]
            fat.text = foodDetails[3]
        }
    }

    companion object {
        const val EXTRA_FOOD_NAME = "extra_food_name"
    }

}