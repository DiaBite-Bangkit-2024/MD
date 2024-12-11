package com.capstone.diabite.ui.dashboard


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.diabite.db.ApiClient
import com.capstone.diabite.db.DataResult
import com.capstone.diabite.db.responses.ProfileResponse
import kotlinx.coroutines.launch
import retrofit2.HttpException

class DashboardViewModel : ViewModel() {
    val name = MutableLiveData<String>()
    private val _userProfile = MutableLiveData<DataResult<ProfileResponse>>()
    val userProfile: LiveData<DataResult<ProfileResponse>> get() = _userProfile

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    private val _tokenExpired = MutableLiveData<Boolean>()
    val tokenExpired: LiveData<Boolean> get() = _tokenExpired



    fun fetchUserProfile(token: String) {
        viewModelScope.launch {
            _userProfile.value = DataResult.Loading
            try {
                val profile = ApiClient.getApiService2().getProfile("Bearer $token")
                _userProfile.value = DataResult.Success(profile)
            } catch (e: HttpException) {
                if (e.code() == 403) {
                    _userProfile.value = DataResult.Error("Token expired, please login again.")
                    _tokenExpired.postValue(true)
                } else {
                    _userProfile.value = DataResult.Error(e.message ?: "Error fetching profile")
                }
            } catch (e: Exception) {
                _userProfile.value = DataResult.Error(e.message ?: "An error occurred")
            }
        }
    }
}