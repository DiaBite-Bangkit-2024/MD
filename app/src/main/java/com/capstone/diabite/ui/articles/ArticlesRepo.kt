package com.capstone.diabite.ui.articles

import com.capstone.diabite.db.ApiService
import com.capstone.diabite.db.NewsResponse
import retrofit2.Response

class ArticlesRepo(private val apiService: ApiService) {
    suspend fun getNews(query: String): Response<NewsResponse> {
        return apiService.getNews(query = query)
    }
}