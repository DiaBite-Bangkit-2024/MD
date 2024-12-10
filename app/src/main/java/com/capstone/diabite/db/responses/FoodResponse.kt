package com.capstone.diabite.db.responses

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class FoodResponse(

    @field:SerializedName("tagReq")
    val tagReq: TagsRequest,

    @field:SerializedName("resultCount")
    val resultCount: ResultCount,

    @field:SerializedName("columnNames")
    val columnNames: List<String>,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("results")
    val results: Results
)

data class Results(

    @SerializedName("cluster_0")
    val cluster0: List<FoodItem>?,

    @SerializedName("cluster_1")
    val cluster1: List<FoodItem>?,

    @SerializedName("cluster_2")
    val cluster2: List<FoodItem>?
)

data class ResultCount(

    @field:SerializedName("cluster_0")
    val cluster0: Int,

    @field:SerializedName("cluster_1")
    val cluster1: Int,

    @field:SerializedName("cluster_2")
    val cluster2: Int
)

data class TagsRequest(
    @field:SerializedName("tags")
    val tags: List<String>
)

data class TagsResponse(

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("tags")
    val tags: List<String>
)

@Parcelize
data class FoodItem(
    @SerializedName("food")
    val name: String,

    @SerializedName("Caloric Value")
    val caloricValue: String,

    @SerializedName("Fat")
    val fat: String,

    @SerializedName("Saturated Fats")
    val saturatedFats: String,

    @SerializedName("Monounsaturated Fats")
    val monounsaturatedFats: String,

    @SerializedName("Polyunsaturated Fats")
    val polyunsaturatedFats: String,

    @SerializedName("Carbohydrates")
    val carbohydrates: String,

    @SerializedName("Sugars")
    val sugars: String,

    @SerializedName("Protein")
    val protein: String,

    @SerializedName("Dietary Fiber")
    val dietaryFiber: String,

    @SerializedName("Cholesterol")
    val cholesterol: String,

    @SerializedName("Sodium")
    val sodium: String,

    @SerializedName("Water")
    val water: String,

    @SerializedName("Vitamin A")
    val vitaminA: String,

    @SerializedName("Vitamin B1")
    val vitaminB1: String,

    @SerializedName("Vitamin B11")
    val vitaminB11: String,

    @SerializedName("Vitamin B12")
    val vitaminB12: String,

    @SerializedName("Vitamin B2")
    val vitaminB2: String,

    @SerializedName("Vitamin B3")
    val vitaminB3: String,

    @SerializedName("Vitamin B5")
    val vitaminB5: String,

    @SerializedName("Vitamin B6")
    val vitaminB6: String,

    @SerializedName("Vitamin C")
    val vitaminC: String,

    @SerializedName("Vitamin D")
    val vitaminD: String,

    @SerializedName("Vitamin E")
    val vitaminE: String,

    @SerializedName("Vitamin K")
    val vitaminK: String,

    @SerializedName("Calcium")
    val calcium: String,

    @SerializedName("Copper")
    val copper: String,

    @SerializedName("Iron")
    val iron: String,

    @SerializedName("Magnesium")
    val magnesium: String,

    @SerializedName("Manganese")
    val manganese: String,

    @SerializedName("Phosphorus")
    val phosphorus: String,

    @SerializedName("Potassium")
    val potassium: String,

    @SerializedName("Selenium")
    val selenium: String,

    @SerializedName("Zinc")
    val zinc: String,

    @SerializedName("Nutrition Density")
    val nutritionDensity: String
) : Parcelable
