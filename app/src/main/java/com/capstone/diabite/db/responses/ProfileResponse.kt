package com.capstone.diabite.db.responses

import com.google.gson.annotations.SerializedName
import okhttp3.MultipartBody

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

	@field:SerializedName("age")
	val age: Int,

	@field:SerializedName("height")
	val height: Int,

	@field:SerializedName("probability")
	val probability: Float,

	@field:SerializedName("avatar")
	val avatar: String?
)

data class UpdateProfileRequest(
	val name: String,
	val age: Int,
	val gender: String,
	val height: Int,
	val weight: Int,
	val systolic: Int,
	val diastolic: Int,
	val avatar: MultipartBody.Part?
)