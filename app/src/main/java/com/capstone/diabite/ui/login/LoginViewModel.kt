package com.capstone.diabite.ui.login

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.capstone.diabite.db.ApiClient
import com.capstone.diabite.db.DataResult
import com.capstone.diabite.db.responses.LoginResponse
import com.capstone.diabite.db.responses.OtpResponse
import com.capstone.diabite.db.responses.ProfileResponse
import com.capstone.diabite.db.responses.UpdateProfileRequest
import com.capstone.diabite.db.pref.UserModel
import com.capstone.diabite.db.pref.UserRepository
import com.capstone.diabite.db.responses.ResetPasswordRequest
import com.capstone.diabite.db.responses.ResetPasswordResponse
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.HttpException
import java.io.File

class LoginViewModel(private val repository: UserRepository) : ViewModel() {

    private val _userProfile = MutableLiveData<DataResult<ProfileResponse>>()
    val userProfile: LiveData<DataResult<ProfileResponse>> get() = _userProfile

    private val _resetPassword = MutableLiveData<DataResult<ResetPasswordResponse>>()
    val resetPassword: LiveData<DataResult<ResetPasswordResponse>> = _resetPassword

    private val _currentImageUri = MutableLiveData<Uri?>()
    val currentImageUri: LiveData<Uri?> get() = _currentImageUri

    fun setImageUri(uri: Uri) {
        _currentImageUri.value = uri
    }

    fun updateUserProfile(updateProfileRequest: UpdateProfileRequest) {
        _userProfile.value = DataResult.Loading
        viewModelScope.launch {
            try {
                val response = repository.editUserProfile(updateProfileRequest)
                _userProfile.value = DataResult.Success(response)
                Log.d("UpdateProfile", "Profile updated successfully: ${response.profile}")
            } catch (e: Exception) {
                _userProfile.value = DataResult.Error(e.message ?: "Error updating profile")
                Log.e("UpdateProfile", "Error updating profile: ${e.message}")
            }
        }
    }

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

    fun register(
        name: String,
        email: String,
        password: String
    ): LiveData<DataResult<LoginResponse>> = liveData {
        emit(DataResult.Loading)
        try {
            val response = ApiClient.getApiService2().register(name, email, password)
            if (!response.error) {
                emit(DataResult.Success(response))
            } else {
                emit(DataResult.Error(response.message))
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
            emit(DataResult.Error(errorMessage))
        } catch (e: Exception) {
            emit(DataResult.Error(e.message ?: "An error occurred"))
        }
    }

    fun resetPass(email: String, otp: String, newPassword: String) {
        viewModelScope.launch {
            _resetPassword.value = try {
                val request = ResetPasswordRequest(email, otp, newPassword)
                val response = ApiClient.getApiService2().resetPassword(request)
                DataResult.Success(response)
            } catch (e: Exception) {
                DataResult.Error(e.message ?: "Failed to reset password.")
            }
        }
    }

    fun forgotPass(
        email: String
    ): LiveData<DataResult<LoginResponse>> = liveData {
        emit(DataResult.Loading)
        try {
            val response = ApiClient.getApiService2().forgotPass(email)
            if (!response.error) {
                emit(DataResult.Success(response))
            } else {
                emit(DataResult.Error(response.message))
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
            emit(DataResult.Error(errorMessage))
        } catch (e: Exception) {
            emit(DataResult.Error(e.message ?: "An error occurred"))
        }
    }

    fun verifyOtp(email: String, otp: String): LiveData<DataResult<OtpResponse>> = liveData {
        emit(DataResult.Loading)
        try {
            val response = ApiClient.getApiService2().verifyOtp(email, otp)
            if (response.message.isNotEmpty()) {
                if (response.message.isNotEmpty()) {
                    val token = response.token
                    val userModel = UserModel(
                        email = email,
                        token = token,
                        isLogin = false
                    )
                    repository.saveSession(userModel)
                    emit(DataResult.Success(response))
                } else {
                    emit(DataResult.Error(response.message))
                }
            } else {
                emit(DataResult.Error(response.message))
            }        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorMessage = errorBody?.let {
                try {
                    val json = JSONObject(it)
                    json.getString("message")
                } catch (ex: Exception) {
                    "An error occurred"
                }
            } ?: "An error occurred"
            emit(DataResult.Error(errorMessage))
        } catch (e: Exception) {
            emit(DataResult.Error(e.message ?: "An unexpected error occurred"))
        }
    }

    fun resendOtp(email: String, otp: String): LiveData<DataResult<OtpResponse>> = liveData {
        emit(DataResult.Loading)
        try {
            val response = ApiClient.getApiService2().resendOtp(email, otp)
            emit(DataResult.Success(response))
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
            emit(DataResult.Error(errorMessage))
        } catch (e: Exception) {
            emit(DataResult.Error(e.message ?: "An unexpected error occurred"))
        }
    }

    fun login(email: String, password: String): LiveData<DataResult<LoginResponse>> = liveData {
        emit(DataResult.Loading)
        try {
            val response = ApiClient.getApiService2().login(email, password)
            if (!response.error) {
                val loginResult = response.loginResult
                val userModel = UserModel(
                    email = email,
                    token = loginResult.token,
                    isLogin = true
                )
                repository.saveSession(userModel)
                emit(DataResult.Success(response))
            } else {
                emit(DataResult.Error(response.message))
            }
        } catch (e: Exception) {
            emit(DataResult.Error(e.message ?: "An error has occurred"))
        }
    }
}