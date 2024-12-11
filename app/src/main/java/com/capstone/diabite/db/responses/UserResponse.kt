package com.capstone.diabite.db.responses

import com.google.gson.annotations.SerializedName

data class UserResponse(

    @field:SerializedName("data")
    val data: Data,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("error")
    val error: Boolean
)

data class Data(

    @field:SerializedName("systolic")
    val systolic: Int,

    @field:SerializedName("diastolic")
    val diastolic: Int,

    @field:SerializedName("gender")
    val gender: String,

    @field:SerializedName("weight")
    val weight: Int,

    @field:SerializedName("email")
    val email: String,

    @field:SerializedName("age")
    val age: Int,

    @field:SerializedName("height")
    val height: Int
)
