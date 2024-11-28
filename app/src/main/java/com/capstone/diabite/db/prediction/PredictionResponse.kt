package com.capstone.diabite.db.prediction

import com.google.gson.annotations.SerializedName

data class PredictionResponse(

	@field:SerializedName("data")
	val data: Data,

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("prediction")
	val prediction: Double,

	@field:SerializedName("message")
	val message: String
)

data class Outputs(

	@field:SerializedName("desc")
	val desc: String,

	@field:SerializedName("example")
	val example: Any
)

data class Inputs(

	@field:SerializedName("input_sequence")
	val inputSequence: List<String>,

	@field:SerializedName("example")
	val example: Example
)

data class Data(

	@field:SerializedName("outputs")
	val outputs: Outputs,

	@field:SerializedName("inputs")
	val inputs: Inputs,

	@field:SerializedName("desc")
	val desc: Desc
)

data class Example(

	@field:SerializedName("non_diabet")
	val nonDiabet: List<Int>,

	@field:SerializedName("pre_diabet")
	val preDiabet: List<Int>,

	@field:SerializedName("diabet")
	val diabet: List<Int>
)

data class Desc(

	@field:SerializedName("Stroke")
	val stroke: String,

	@field:SerializedName("HeartDiseaseorAttack")
	val heartDiseaseorAttack: String,

	@field:SerializedName("HighBP")
	val highBP: String,

	@field:SerializedName("HighChol")
	val highChol: String,

	@field:SerializedName("DiffWalk")
	val diffWalk: String,

	@field:SerializedName("GenHlth")
	val genHlth: String,

	@field:SerializedName("PhysHlth")
	val physHlth: String,

	@field:SerializedName("Age")
	val age: String,

	@field:SerializedName("BMI")
	val bMI: String,

	@field:SerializedName("MentHlth")
	val mentHlth: String
)
