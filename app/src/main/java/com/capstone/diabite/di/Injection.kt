package com.capstone.diabite.di

import android.content.Context
import com.capstone.diabite.db.ApiClient
import com.capstone.diabite.db.pref.UserRepository
import com.faraflh.storyapp.data.pref.UserPreference
import com.faraflh.storyapp.data.pref.dataStore

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        return UserRepository.getInstance(pref)
    }
}