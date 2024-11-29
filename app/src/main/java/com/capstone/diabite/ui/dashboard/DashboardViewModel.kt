package com.capstone.diabite.ui.dashboard

import android.app.Application
import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.diabite.db.ApiClient
import com.capstone.diabite.db.ApiService
import com.capstone.diabite.db.DataResult
import com.capstone.diabite.db.ProfileRepository
import com.capstone.diabite.db.ProfileResponse
import com.capstone.diabite.db.UserResponse
import com.capstone.diabite.db.prediction.PredictionResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DashboardViewModel : ViewModel() {
    val name = MutableLiveData<String>()
    private val _userProfile = MutableLiveData<DataResult<ProfileResponse>>()
    val userProfile: LiveData<DataResult<ProfileResponse>> get() = _userProfile

//    private val repository = ProfileRepository()

    private val _predictionData = MutableLiveData<DataResult<PredictionResponse>>()
    val predictionData: LiveData<DataResult<PredictionResponse>> get() = _predictionData

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    fun fetchUserProfile(token: String) {
        viewModelScope.launch {
            try {
                val profile = ApiClient.getApiService2().getProfile("Bearer $token")
                _userProfile.value = DataResult.Success(profile)
            } catch (e: Exception) {
                Log.e("UserProfileViewModel", "Error fetching profile: ${e.message}")
                val error = e.message ?: "Error fetching profile: ${e.message}"
                _userProfile.value = DataResult.Error(error)
            }
        }
    }

    fun fetchPrediction() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = ApiClient.getApiService2().getPrediction()
                if (!response.error) {
                    _predictionData.value = DataResult.Success(response)
                } else {
                    _errorMessage.postValue("Error: ${response.message}")
                }
            } catch (e: Exception) {
                _errorMessage.postValue("Exception: ${e.localizedMessage}")
                Log.e(TAG, "Error fetching profile: ${e.message}")

            }
        }
    }
}