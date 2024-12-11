package com.capstone.diabite.view.quiz

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class QuizViewModel(application: Application) : AndroidViewModel(application) {

    private val sharedPreferences =
        application.getSharedPreferences("streak_prefs", Context.MODE_PRIVATE)

    private val _streakCount = MutableLiveData<Int>()
    val streakCount: LiveData<Int> get() = _streakCount

    private val _streakActive = MutableLiveData<Boolean>()
    val streakActive: LiveData<Boolean> get() = _streakActive

    init {
        _streakCount.value = sharedPreferences.getInt("streak_count", 0)
        _streakActive.value = sharedPreferences.getBoolean("streak_active", false)
        resetStreak()
    }

    fun incrementStreak() {
        val lastUpdated = sharedPreferences.getLong("streak_last_updated", 0)
        val currentTime = System.currentTimeMillis()
        val oneDayInMillis = 24 * 60 * 60 * 1000L
        val newStreakCount = (_streakCount.value ?: 0) + 1

        if (currentTime - lastUpdated > oneDayInMillis) {
            _streakActive.value = true
            _streakCount.value = newStreakCount
            sharedPreferences.edit()
                .putLong("streak_last_updated", currentTime)
                .putInt("streak_count", newStreakCount)
                .putBoolean("streak_active", true)
                .apply()
        } else if (currentTime - lastUpdated < oneDayInMillis) {
            _streakActive.value = true
            _streakCount.value = _streakCount.value
            sharedPreferences.edit()
                .putLong("streak_last_updated", currentTime)
                .putInt("streak_count", _streakCount.value!!)
                .putBoolean("streak_active", true)
                .apply()
        }
    }

    private fun resetStreak() {
        val lastUpdated = sharedPreferences.getLong("streak_last_updated", 0)
        val currentTime = System.currentTimeMillis()
        val oneDayInMillis = 24 * 60 * 60 * 1000L
        val twoDayInMillis = 2 * 24 * 60 * 60 * 1000L

        if (currentTime - lastUpdated > twoDayInMillis) {
            _streakCount.value = 0
            _streakActive.value = false
            sharedPreferences.edit()
                .putInt("streak_count", 0)
                .putBoolean("streak_active", false)
                .apply()
        } else if (currentTime - lastUpdated > oneDayInMillis) {
            _streakActive.value = false
            sharedPreferences.edit()
                .putBoolean("streak_active", false)
                .apply()
        } else {
            _streakActive.value = sharedPreferences.getBoolean("streak_active", false)
        }
    }
}
