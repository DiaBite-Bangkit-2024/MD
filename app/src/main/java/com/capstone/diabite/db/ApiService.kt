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
}
