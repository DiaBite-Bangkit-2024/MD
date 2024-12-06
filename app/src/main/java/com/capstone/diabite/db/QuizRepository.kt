package com.capstone.diabite.db

import com.capstone.diabite.BuildConfig
import com.capstone.diabite.db.responses.QuizRequest
import com.capstone.diabite.db.responses.QuizResponse

class QuizRepository {
    private val apiService = ApiClient.getApiService3()

    suspend fun generateTrivia(): QuizResponse {
        val quizRequest = QuizRequest(
            count = 5,
            difficulty = "EASY",
            text = "Diabetes is a chronic health condition..."
        )

        return apiService.generateTrivia(
            quizRequest,
            apiKey = BuildConfig.QUIZ_API_KEY,
            apiHost = "ai-trivia-questions-generator.p.rapidapi.com"
        )
    }
}
