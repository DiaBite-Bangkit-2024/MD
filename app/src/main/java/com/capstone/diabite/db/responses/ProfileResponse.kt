package com.capstone.diabite.db.responses

import com.google.gson.annotations.SerializedName

data class ProfileResponse(

    @field:SerializedName("profile")
	val profile: Profile,

    @field:SerializedName("message")
	val message: String
)

data class Profile(

	@field:SerializedName("systolic")
	val systolic: Int,

	@field:SerializedName("diastolic")
	val diastolic: Int,

	@field:SerializedName("gender")
	val gender: String,

	@field:SerializedName("password")
	val password: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("weight")
	val weight: Int,

	@field:SerializedName("email")
	val email: String,

	@field:SerializedName("age")
	val age: Int,

	@field:SerializedName("height")
	val height: Int
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