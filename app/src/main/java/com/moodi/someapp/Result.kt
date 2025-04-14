package com.moodi.someapp

import com.moodi.someapp.remote.ErrorResponse

sealed class Result<T>(val result: T?) {
    data class Success<T>(val data: T) : Result<T>(data)
    data class RemoteError<T>(val remoteError: ErrorResponse) : Result<T>(null)
    data class Error<T>(val error: Exception, val data: T?) : Result<T>(data)
}
