package com.capstone.diabite.ui.dashboard

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.diabite.db.ApiClient
import com.capstone.diabite.db.DataResult
import com.capstone.diabite.db.responses.ProfileResponse
import kotlinx.coroutines.launch

class DashboardViewModel : ViewModel() {
    val name = MutableLiveData<String>()
    private val _userProfile = MutableLiveData<DataResult<ProfileResponse>>()
    val userProfile: LiveData<DataResult<ProfileResponse>> get() = _userProfile

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
}