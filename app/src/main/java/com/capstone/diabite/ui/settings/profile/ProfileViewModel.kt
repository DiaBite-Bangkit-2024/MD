//package com.capstone.diabite.ui.settings.profile
//
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.ViewModel
//import com.capstone.diabite.db.UserData
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.withContext
//
//class ProfileViewModel : ViewModel(){
//    private val _personalInfo = MutableLiveData<UserData>()
//    val personalInfo: LiveData<UserData> = _personalInfo
//
//    init {
//        loadPersonalInfo()
//    }
//
//    private fun loadPersonalInfo() {
//        _personalInfo.value = UserData(
//            name = "John Doe",
//            newEmail = "johndoe@example.com",
//            password = "password123",
//            age = 30,
//            gender = "Male",
//            height = 175,
//            weight = 70,
//            systolic = 120,
//            diastolic = 80
//        )
//    }
//
//    suspend fun savePersonalInfo(
//        name: String,
//        newEmail: String,
//        password: String,
//        age: Int,
//        gender: String,
//        height: Int,
//        weight: Int,
//        systolic: Int,
//        diastolic: Int
//    ) {
//        withContext(Dispatchers.IO) {
//            _personalInfo.postValue(
//                UserData(name, newEmail, password, age, gender, height, weight, systolic, diastolic)
//            )
//        }
//    }
//}