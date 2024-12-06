package com.capstone.diabite.view.food

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.capstone.diabite.R
import com.capstone.diabite.databinding.ActivityDetailBinding
import com.capstone.diabite.db.responses.FoodItem

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val foodItem = intent.getParcelableExtra<FoodItem>(FOOD_ID)

        foodItem?.let {
            binding.apply {
                foodName.text = it.name
                calNum.text = it.caloricValue

                val nutrientBindings = listOf(
                    fatVal to Pair(R.string.gram, it.fat),
                    satFatVal to Pair(R.string.gram, it.saturatedFats),
                    monoFatVal to Pair(R.string.gram, it.monounsaturatedFats),
                    polyFatVal to Pair(R.string.gram, it.polyunsaturatedFats),
                    cholesterolVal to Pair(R.string.mg, it.cholesterol),
                    sodiumVal to Pair(R.string.gram, it.sodium),
                    carbVal to Pair(R.string.gram, it.carbohydrates),
                    dietFiberVal to Pair(R.string.gram, it.dietaryFiber),
                    sugarVal to Pair(R.string.gram, it.sugars),
                    proteinVal to Pair(R.string.gram, it.protein),
                    waterVal to Pair(R.string.mg, it.water),
                    calciumVal to Pair(R.string.mg, it.calcium),
                    copperVal to Pair(R.string.mg, it.copper),
                    ironVal to Pair(R.string.mg, it.iron),
                    magnesiumVal to Pair(R.string.mg, it.magnesium),
                    manganeseVal to Pair(R.string.mg, it.manganese),
                    phosphorusVal to Pair(R.string.mg, it.phosphorus),
                    potassiumVal to Pair(R.string.mg, it.potassium),
                    seleniumVal to Pair(R.string.mg, it.selenium),
                    zincVal to Pair(R.string.mg, it.zinc),
                    vitAVal to Pair(R.string.mg, it.vitaminA),
                    vitB1Val to Pair(R.string.mg, it.vitaminB1),
                    vitB11Val to Pair(R.string.mg, it.vitaminB11),
                    vitB12Val to Pair(R.string.mg, it.vitaminB12),
                    vitB2Val to Pair(R.string.mg, it.vitaminB2),
                    vitB3Val to Pair(R.string.mg, it.vitaminB3),
                    vitB5Val to Pair(R.string.mg, it.vitaminB5),
                    vitB6Val to Pair(R.string.mg, it.vitaminB6),
                    vitCVal to Pair(R.string.mg, it.vitaminC),
                    vitDVal to Pair(R.string.mg, it.vitaminD),
                    vitEVal to Pair(R.string.mg, it.vitaminE),
                    vitKVal to Pair(R.string.mg, it.vitaminK)
                )

                nutrientBindings.forEach { (textView, data) ->
                    textView.text = getString(data.first, data.second)
                }

                btnClose.setOnClickListener { onBackPressedDispatcher.onBackPressed() }
            }
        }
    }

    companion object {
        const val FOOD_ID = "foodItem"
    }
}
