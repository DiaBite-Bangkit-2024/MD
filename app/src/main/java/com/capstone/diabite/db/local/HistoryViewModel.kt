package com.capstone.diabite.db.local

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HistoryViewModel(application: Application) : AndroidViewModel(application) {
    private val historyDao = HistoryDatabase.getDatabase(application).historyDao()

    private val historyRepository: HistoryRepository = HistoryRepository(historyDao)
    val allHistory: LiveData<List<HistoryEntity>> = historyRepository.getAllPredictions()

    fun addHistory(prediction: Int, summary: String) {
        val history = HistoryEntity(prediction = prediction, summary = summary)
        viewModelScope.launch {
            historyDao.insertPrediction(history)
        }
    }
    fun deleteHistory(history: HistoryEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            historyRepository.delete(history)
        }
    }

    fun deleteAllHistory() {
        viewModelScope.launch(Dispatchers.IO) {
            historyRepository.deleteAll()
        }
    }
}
