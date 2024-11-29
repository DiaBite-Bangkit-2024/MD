package com.capstone.diabite.db.prediction

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.diabite.db.ApiClient
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.HttpException

class PredictionViewModel : ViewModel() {

    private val _response = MutableLiveData<AnalyzeResponse>()
    val response: LiveData<AnalyzeResponse> = _response

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun postAnalyzeData(token: String, request: PredictionRequest) {
        viewModelScope.launch {
            if (token.isEmpty()) {
                _errorMessage.postValue("Token is missing. Please login.")
                return@launch
            }

            try {
                val response = ApiClient.getApiService2().postPrediction("Bearer $token", request)

                if (!response.error) {
                    _response.postValue(response)
                } else {
                    _errorMessage.postValue(response.message)
                }
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val errorMessage = errorBody?.let {
                    try {
                        val json = JSONObject(it)
                        json.getString("message")
                    } catch (ex: Exception) {
                        "An error occurred"
                    }
                } ?: "An error occurred"
                _errorMessage.postValue(errorMessage)
            } catch (e: Exception) {
                _errorMessage.postValue(e.message ?: "An error occurred")
            }
        }
    }

}