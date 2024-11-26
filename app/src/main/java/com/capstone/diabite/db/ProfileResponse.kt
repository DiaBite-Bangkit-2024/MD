package com.capstone.diabite.db

import com.google.gson.annotations.SerializedName

data class ProfileResponse(

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("newEmail")
    val newEmail: String,

    @field:SerializedName("password")
    val password: String,
    @field:SerializedName("age")
    val age: Int,

    @field:SerializedName("gender")
    val gender: String,

    @field:SerializedName("height")
    val height: Int,

    @field:SerializedName("weight")
    val weight: Int,

    @field:SerializedName("systolic")
    val systolic: Int,

    @field:SerializedName("diastolic")
    val diastolic: Int
)

data class UpdateProfileRequest(
    val name: String,
    val newEmail: String,
    val password: String,
    val age: Int,
    val gender: String,
    val height: Int,
    val weight: Int,
    val systolic: Int,
    val diastolic: Int
)
