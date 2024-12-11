package com.capstone.diabite.db.pref

data class UserModel(
    val email: String,
    val token: String,
    val isLogin: Boolean = false
)