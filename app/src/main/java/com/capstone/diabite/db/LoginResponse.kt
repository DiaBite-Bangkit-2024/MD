package com.capstone.diabite.db

data class LoginResponse(
	val loginResult: LoginResult,
	val message: String,
	val error: Boolean
)
data class LoginResult(
	val name: String,
	val userId: Int,
	val token: String
)

