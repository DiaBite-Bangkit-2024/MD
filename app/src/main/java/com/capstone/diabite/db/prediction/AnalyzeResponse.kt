package com.capstone.diabite.db.prediction

import com.google.gson.annotations.SerializedName

data class AnalyzeResponse(

    @field:SerializedName("affected_rows")
    val affectedRows: Int,

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("prediction")
    val prediction: Double,

    @field:SerializedName("message")
    val message: String
)

data class PredictionRequest(
    val input: List<Any>
)

