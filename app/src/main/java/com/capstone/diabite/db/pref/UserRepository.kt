package com.capstone.diabite.db.pref

import com.capstone.diabite.db.ApiClient
import com.capstone.diabite.db.ApiService
import com.capstone.diabite.db.responses.ProfileResponse
import com.capstone.diabite.db.responses.UpdateProfileRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody

class UserRepository private constructor(
    private val userPreference: UserPreference
) {

    private val apiService: ApiService = ApiClient.getApiService2()

    suspend fun editUserProfile(updateProfileRequest: UpdateProfileRequest): ProfileResponse {
        val token = userPreference.getToken().firstOrNull()
        if (token.isNullOrEmpty()) throw Exception("Token is missing")

        val name = updateProfileRequest.name.toRequestBody("text/plain".toMediaType())
        val age = updateProfileRequest.age.toString().toRequestBody("text/plain".toMediaType())
        val gender = updateProfileRequest.gender.toRequestBody("text/plain".toMediaType())
        val height = updateProfileRequest.height.toString().toRequestBody("text/plain".toMediaType())
        val weight = updateProfileRequest.weight.toString().toRequestBody("text/plain".toMediaType())
        val systolic = updateProfileRequest.systolic.toString().toRequestBody("text/plain".toMediaType())
        val diastolic = updateProfileRequest.diastolic.toString().toRequestBody("text/plain".toMediaType())

        val avatarPart = updateProfileRequest.avatar

        return apiService.editUserProfile("Bearer $token", name, age, gender, height, weight, systolic, diastolic, avatarPart)
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