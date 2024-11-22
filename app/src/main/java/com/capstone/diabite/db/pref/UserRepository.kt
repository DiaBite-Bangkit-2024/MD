package com.capstone.diabite.db.pref

import com.faraflh.storyapp.data.pref.UserPreference
import kotlinx.coroutines.flow.Flow

class UserRepository private constructor(
    private val userPreference: UserPreference
) {

    suspend fun saveAuthToken(token: String) {
        userPreference.saveAuthToken(token)
    }

    fun getAuthToken(): Flow<String?> {
        return userPreference.authToken
    }

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    suspend fun saveOtp(otp: String) {
        userPreference.saveOtp(otp)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    fun getOtp(): Flow<String?> {
        return userPreference.getOtp()
    }

    suspend fun logout() {
        userPreference.logout()
    }


    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            userPreference: UserPreference
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(userPreference)
            }.also { instance = it }
    }
}