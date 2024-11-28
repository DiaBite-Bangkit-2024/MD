package com.capstone.diabite.db.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "predict_history")
data class HistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val prediction: Int,
    val summary: String,
    val timestamp: Long = System.currentTimeMillis()
)