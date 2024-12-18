package com.capstone.diabite.db

import com.capstone.diabite.BuildConfig
import com.capstone.diabite.db.prediction.AnalyzeResponse
import com.capstone.diabite.db.prediction.PredictionRequest
import com.capstone.diabite.db.responses.FoodResponse
import com.capstone.diabite.db.responses.LoginResponse
import com.capstone.diabite.db.responses.NewsResponse
import com.capstone.diabite.db.responses.OtpResponse
import com.capstone.diabite.db.responses.ProfileResponse
import com.capstone.diabite.db.responses.QuizRequest
import com.capstone.diabite.db.responses.QuizResponse
import com.capstone.diabite.db.responses.ResetPasswordRequest
import com.capstone.diabite.db.responses.ResetPasswordResponse
import com.capstone.diabite.db.responses.TagsRequest
import com.capstone.diabite.db.responses.TagsResponse
import com.capstone.diabite.db.responses.UserResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
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

    @POST("fromText/trueFalse")
    suspend fun generateTrivia(
        @Body quizRequest: QuizRequest,
        @Header("x-rapidapi-key") apiKey: String,
        @Header("x-rapidapi-host") apiHost: String
    ): QuizResponse

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
    @POST("auth/forget-pw")
    suspend fun forgotPass(
        @Field("email") email: String
    ): LoginResponse

    @POST("auth/reset-pw")
    suspend fun resetPassword(@Body request: ResetPasswordRequest): ResetPasswordResponse

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

    @Multipart
    @PATCH("auth/edit-profile")
    suspend fun editUserProfile(
        @Header("Authorization") token: String,
        @Part("name") name: RequestBody,
        @Part("age") age: RequestBody,
        @Part("gender") gender: RequestBody,
        @Part("height") height: RequestBody,
        @Part("weight") weight: RequestBody,
        @Part("systolic") systolic: RequestBody,
        @Part("diastolic") diastolic: RequestBody,
        @Part avatar: MultipartBody.Part?
    ): ProfileResponse

    @POST("predict")
    suspend fun postPrediction(
        @Header("Authorization") token: String,
        @Body request: PredictionRequest
    ): AnalyzeResponse

    @GET("food/tags")
    suspend fun getTags(): TagsResponse

    @POST("food")
    suspend fun getFoodRec(
        @Body request: TagsRequest
    ): FoodResponse

}
