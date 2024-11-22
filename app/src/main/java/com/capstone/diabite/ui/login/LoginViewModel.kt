package com.capstone.diabite.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.capstone.diabite.db.ApiClient
import com.capstone.diabite.db.DataResult
import com.capstone.diabite.db.LoginResponse
import com.capstone.diabite.db.pref.UserModel
import com.capstone.diabite.db.pref.UserRepository
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.HttpException

class LoginViewModel(private val repository: UserRepository) : ViewModel() {

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun getOtp(): LiveData<String?> {
        return repository.getOtp().asLiveData()
    }

    fun saveOtp(otp: String) {
        viewModelScope.launch {
            repository.saveOtp(otp)
        }
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

    fun verifyOtp(otp: String): LiveData<DataResult<LoginResponse>> = liveData {
        emit(DataResult.Loading)
        try {
            val response = ApiClient.getApiService2().verifyOtp(otp)
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