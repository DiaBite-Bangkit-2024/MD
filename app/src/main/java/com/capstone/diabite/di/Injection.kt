package com.capstone.diabite.di

import android.content.Context
import com.capstone.diabite.db.pref.UserRepository
import com.capstone.diabite.db.pref.UserPreference
import com.capstone.diabite.db.pref.dataStore

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        return UserRepository.getInstance(pref)
    }
}