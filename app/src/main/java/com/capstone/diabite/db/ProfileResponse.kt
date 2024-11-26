package com.capstone.diabite.db

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
