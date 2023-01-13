package com.tinude.newsheadlines.network

sealed class ResultWrapper<out T> {
    data class Success<out T>(val value: T) : ResultWrapper<T>()
    data class Loading(val loading: Boolean) : ResultWrapper<Boolean>()
    data class GenericError(val code: Int? = null, val error: String) : ResultWrapper<Nothing>()
}
