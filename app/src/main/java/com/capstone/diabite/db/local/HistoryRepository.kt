package com.capstone.diabite.db.local

import androidx.lifecycle.LiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class HistoryRepository(private val historyDao: HistoryDao) {
    fun getAllPredictions(): LiveData<List<HistoryEntity>> = historyDao.getAllPredictions()

}