package com.capstone.diabite.ui.articles

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ArticlesVMFactory(private val repository: ArticlesRepo) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ArticlesViewModel::class.java)) {
            return ArticlesViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
