package com.capstone.diabite.db.local

import androidx.lifecycle.LiveData

class HistoryRepository(private val historyDao: HistoryDao) {
    fun getAllPredictions(): LiveData<List<HistoryEntity>> = historyDao.getAllPredictions()
}