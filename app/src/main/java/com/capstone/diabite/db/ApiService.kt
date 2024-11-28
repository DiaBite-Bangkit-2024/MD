package com.capstone.diabite.db

import com.capstone.diabite.BuildConfig
import com.capstone.diabite.db.prediction.AnalyzeResponse
import com.capstone.diabite.db.prediction.PredictionRequest
import com.capstone.diabite.db.prediction.PredictionResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @GET("search.json")
    suspend fun getNews(
        @Query("engine") engine: String = "google_news",
        @Query("q") query: String = "diabetes",
        @Query("hl") language: String = "en",
        @Query("api_key") apiKey: String = BuildConfig.NEWS_API_KEY
    ): Response<NewsResponse>

    @FormUrlEncoded
    @POST("auth/verify-otp")
    suspend fun verifyOtp(
        @Field("email") email: String,
        @Field("otp") otp: String
    ): OtpResponse

    @FormUrlEncoded
    @POST("auth/resend-otp")
    suspend fun resendOtp(
        @Field("email") email: String,
        @Field("otp") otp: String
    ): OtpResponse

    @FormUrlEncoded
    @POST("auth/register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    @FormUrlEncoded
    @POST("auth/login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    @FormUrlEncoded
    @POST("auth/save-profile")
    suspend fun saveProfile(
        @Field("email") email: String,
        @Field("age") age: Int,
        @Field("gender") gender: String,
        @Field("weight") weight: Int,
        @Field("height") height: Int,
        @Field("systolic") systolic: Int,
        @Field("diastolic") diastolic: Int
    ): UserResponse

    @GET("auth/user-profile")
    suspend fun getProfile(
        @Header("Authorization") token: String
    ): ProfileResponse

    @PATCH("auth/edit-profile")
    suspend fun editUserProfile(
        @Header("Authorization") token: String,
        @Body updateProfileRequest: UpdateProfileRequest
    ): ProfileResponse

    @POST("predict")
    suspend fun postPrediction(
        @Header("Authorization") token: String,
        @Body request: PredictionRequest
    ): AnalyzeResponse

    @GET("predict")
    suspend fun getPrediction(): PredictionResponse

}
