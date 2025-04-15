package com.moodi.someapp

sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Failure(val data: String, val error: Exception) : Result<Nothing>()
}
