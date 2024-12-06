package com.capstone.diabite.db.responses

data class ResetPasswordResponse(
    val message: String,
    val error: Boolean
)

data class ResetPasswordRequest(
    val email: String,
    val otp: String,
    val newPassword: String
)
