package com.capstone.diabite.db.local

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface HistoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPrediction(history: HistoryEntity)

    @Query("SELECT * FROM predict_history ORDER BY timestamp DESC")
    fun getAllPredictions(): LiveData<List<HistoryEntity>>
}