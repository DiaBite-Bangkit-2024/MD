package com.capstone.diabite.ui.articles

import android.app.Application
import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.diabite.db.ApiClient
import com.capstone.diabite.db.DataResult
import com.capstone.diabite.db.responses.NewsResultsItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ArticlesViewModel(private val repository: ArticlesRepo) : ViewModel() {

    private val _newsData = MutableLiveData<DataResult<List<NewsResultsItem>>>()
    val newsData: LiveData<DataResult<List<NewsResultsItem>>> = _newsData

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage


    private val _tags = MutableStateFlow<List<String>>(emptyList())
    val tags: StateFlow<List<String>> get() = _tags

    private val _foodClusters = MutableLiveData<Map<String, List<String>>>()
    val foodClusters: LiveData<Map<String, List<String>>> = _foodClusters


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

    fun loadTags() {
        viewModelScope.launch {
            val tagList = repository.fetchTags()
            _tags.value = tagList
        }
    }

    fun fetchFoodByTag(tagsRequest: TagsRequest) {
        viewModelScope.launch {
            try {
                val response = repository.getFoodRec(tagsRequest)
                Log.d("API Response", response.toString())

                val cluster0 = response.results.cluster0?.map { it.name } ?: emptyList()
                val cluster1 = response.results.cluster1?.map { it.name } ?: emptyList()
                val cluster2 = response.results.cluster2?.map { it.name } ?: emptyList()
                val allClusters = listOf(cluster0, cluster1, cluster2).flatten()

                Log.d("Cluster 0", cluster0.toString())
                Log.d("Cluster 1", cluster1.toString())
                Log.d("Cluster 2", cluster2.toString())

                val clusters = mapOf(
                    "cluster_0" to cluster0,
                    "cluster_1" to cluster1,
                    "cluster_2" to cluster2
                )

                _foodClusters.postValue(clusters)

                allClusters.forEach { foodItem ->
                    Log.d("FoodItem", "Food: $foodItem")
                }
            } catch (e: Exception) {
                Log.e("API Error", e.message ?: "Unknown error")
                _foodClusters.postValue(emptyMap())
            }
        }
    }
}