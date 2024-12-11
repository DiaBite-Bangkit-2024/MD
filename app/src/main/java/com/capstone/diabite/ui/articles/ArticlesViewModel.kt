package com.capstone.diabite.ui.articles

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.diabite.db.DataResult
import com.capstone.diabite.db.responses.TagsRequest
import com.capstone.diabite.db.responses.NewsResultsItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.capstone.diabite.db.responses.FoodItem
import kotlinx.coroutines.launch

class ArticlesViewModel(private val repository: ArticlesRepo) : ViewModel() {

    private val _newsData = MutableLiveData<DataResult<List<NewsResultsItem>>>()
    val newsData: LiveData<DataResult<List<NewsResultsItem>>> = _newsData

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage


    private val _tags = MutableStateFlow<List<String>>(emptyList())
    val tags: StateFlow<List<String>> get() = _tags

    private val _foodClusters =  MutableLiveData<DataResult<Map<String, List<FoodItem>>>>()
    val foodClusters: LiveData<DataResult<Map<String, List<FoodItem>>>> = _foodClusters


    fun fetchNews(query: String) {
        if (_newsData.value !is DataResult.Success) {
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

    fun loadTags() {
        viewModelScope.launch {
            val tagList = repository.fetchTags()
            _tags.value = tagList
        }
    }

    fun fetchFoodByTag(tagsRequest: TagsRequest) {
        viewModelScope.launch {
            _foodClusters.value = DataResult.Loading
            try {
                val response = repository.getFoodRec(tagsRequest)
                Log.d("API Response", response.toString())

                fun mapToFoodItem(food: FoodItem): FoodItem {
                    return FoodItem(
                        name = food.name,
                        caloricValue = food.caloricValue,
                        fat = food.fat,
                        saturatedFats = food.saturatedFats,
                        monounsaturatedFats = food.monounsaturatedFats,
                        polyunsaturatedFats = food.polyunsaturatedFats,
                        carbohydrates = food.carbohydrates,
                        sugars = food.sugars,
                        protein = food.protein,
                        dietaryFiber = food.dietaryFiber,
                        cholesterol = food.cholesterol,
                        sodium = food.sodium,
                        water = food.water,
                        vitaminA = food.vitaminA,
                        vitaminB1 = food.vitaminB1,
                        vitaminB11 = food.vitaminB11,
                        vitaminB12 = food.vitaminB12,
                        vitaminB2 = food.vitaminB2,
                        vitaminB3 = food.vitaminB3,
                        vitaminB5 = food.vitaminB5,
                        vitaminB6 = food.vitaminB6,
                        vitaminC = food.vitaminC,
                        vitaminD = food.vitaminD,
                        vitaminE = food.vitaminE,
                        vitaminK = food.vitaminK,
                        calcium = food.calcium,
                        copper = food.copper,
                        iron = food.iron,
                        magnesium = food.magnesium,
                        manganese = food.manganese,
                        phosphorus = food.phosphorus,
                        potassium = food.potassium,
                        selenium = food.selenium,
                        zinc = food.zinc,
                        nutritionDensity = food.nutritionDensity
                    )
                }

                if (!response.error) {
                    val clusters = mapOf(
                        "cluster_0" to (response.results.cluster0?.map { mapToFoodItem(it) } ?: emptyList()),
                        "cluster_1" to (response.results.cluster1?.map { mapToFoodItem(it) } ?: emptyList()),
                        "cluster_2" to (response.results.cluster2?.map { mapToFoodItem(it) } ?: emptyList())
                    )
                    _foodClusters.value = DataResult.Success(clusters)
                } else {
                    Log.e("API Error", "Failed to fetch food clusters: ${response.message}")
                    _foodClusters.value = DataResult.Error("Failed to fetch food clusters: ${response.message}")
                }

            } catch (e: Exception) {
                Log.e("API Error", e.message ?: "Unknown error")
                _foodClusters.value = DataResult.Error("Error: ${e.message ?: "Unknown error"}")
            }
        }
    }
}