package com.capstone.diabite.db.pref

import com.capstone.diabite.db.ApiClient
import com.capstone.diabite.db.ApiService
import com.capstone.diabite.db.ProfileResponse
import com.capstone.diabite.db.UpdateProfileRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class UserRepository private constructor(
    private val userPreference: UserPreference
) {

    private val apiService: ApiService = ApiClient.getApiService2()

    suspend fun editUserProfile(updateProfileRequest: UpdateProfileRequest): ProfileResponse {
        val token = userPreference.getToken().firstOrNull()
        if (token.isNullOrEmpty()) throw Exception("Token is missing")
        return apiService.editUserProfile("Bearer $token", updateProfileRequest)
    }

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
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