package com.capstone.diabite.db.responses

import com.google.gson.annotations.SerializedName

data class QuizResponse(

	@field:SerializedName("success")
	val success: Boolean,

	@field:SerializedName("count")
	val count: Int,

	@field:SerializedName("trivia")
	val trivia: List<TriviaItem>,

	@field:SerializedName("tokens")
	val tokens: Tokens,

	@field:SerializedName("type")
	val type: String
)

data class TriviaItem(

	@field:SerializedName("answer")
	val answer: Boolean,

	@field:SerializedName("question")
	val question: String
)

data class Tokens(

	@field:SerializedName("output")
	val output: Int,

	@field:SerializedName("input")
	val input: Int
)

data class QuizRequest(
	val count: Int,
	val difficulty: String,
	val text: String
)

