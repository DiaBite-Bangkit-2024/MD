package com.capstone.diabite.ui.articles

import com.capstone.diabite.db.ApiService
import com.capstone.diabite.db.responses.NewsResponse
import com.capstone.diabite.db.responses.FoodResponse
import com.capstone.diabite.db.responses.TagsRequest
import retrofit2.Response

class ArticlesRepo(private val apiService: ApiService) {
    suspend fun getNews(query: String): Response<NewsResponse> {
        return apiService.getNews(query = query)
    }

    suspend fun fetchTags(): List<String> {
        return try {
            val response = apiService.getTags()
            if (!response.error) {
                response.tags
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getFoodRec(request: TagsRequest): FoodResponse {
        return apiService.getFoodRec(request)
    }
}