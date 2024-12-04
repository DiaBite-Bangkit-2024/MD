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

        val foodDetails = intent.getStringArrayListExtra("FOOD_DETAILS") ?: return

        binding.apply {
            tvFoodName.text = foodDetails[0]
            tvCalories.text = "Calories: ${foodDetails[1]}"
            tvProtein.text = "Protein: ${foodDetails[2]}"
            tvFat.text = "Fat: ${foodDetails[3]}"
        }
    }

    companion object {
        const val EXTRA_FOOD_NAME = "extra_food_name"
    }

}