package com.capstone.diabite.db

import com.capstone.diabite.BuildConfig
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

}
