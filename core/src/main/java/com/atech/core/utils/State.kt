package com.atech.core.utils

sealed class State<out T> {
    data class Success<T>(val data: T) : State<T>()
    data class Error(val exception: Exception) : State<Nothing>()
    data object Loading : State<Nothing>()
}