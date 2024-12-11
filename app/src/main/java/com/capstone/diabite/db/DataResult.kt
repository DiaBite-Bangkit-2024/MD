package com.capstone.diabite.db

sealed class DataResult<out R> private constructor() {
    data class Success<out T>(val data: T) : DataResult<T>()
    data class Error<T>(val message: String) : DataResult<T>()
    data object Loading : DataResult<Nothing>()
}