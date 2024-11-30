package com.capstone.diabite.ui.articles

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.diabite.db.DataResult
import com.capstone.diabite.db.responses.NewsResultsItem
import kotlinx.coroutines.launch

class ArticlesViewModel(private val repository: ArticlesRepo) : ViewModel() {

    private val _newsData = MutableLiveData<DataResult<List<NewsResultsItem>>>()
    val newsData: LiveData<DataResult<List<NewsResultsItem>>> = _newsData

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage
    fun fetchNews(query: String) {
        viewModelScope.launch {
            _newsData.value = DataResult.Loading
            try {
                val response = repository.getNews(query)
                if (response.isSuccessful && response.body() != null) {
                    _newsData.value = DataResult.Success(response.body()!!.newsResults)
                } else {

                    Log.e(TAG, "Failed to fetch news")
                    _newsData.value = DataResult.Error("Failed to fetch news: ${response.message()}")
                }
            } catch (e: Exception) {
                _newsData.value = DataResult.Error("Error: ${e.message}")
            }
        }
    }
}