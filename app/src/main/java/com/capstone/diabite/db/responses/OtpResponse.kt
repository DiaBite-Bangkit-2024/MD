package com.capstone.diabite.db.responses

import com.google.gson.annotations.SerializedName
data class OtpResponse(

	@field:SerializedName("otpResult")
	val otpResult: OtpResult,

	@field:SerializedName("message")
	val message: String,

	val token: String
)

data class OtpResult(

	@field:SerializedName("email")
	val email: String,

	@field:SerializedName("otp")
	val otp: String,
)

