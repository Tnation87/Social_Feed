package com.example.data.errors

import com.example.data.isNetworkError
import com.squareup.moshi.JsonDataException
import retrofit2.Call

object ResponseErrorHandler {

    fun <T> handleThrowable(call: Call<T>, t: Throwable): Throwable {
        return when {
            t.isNetworkError() -> ResponseError.NoConnection
            t is JsonDataException -> extractSerializationError(call, t)
            else -> t
        }
    }

    private fun <T> extractSerializationError(call: Call<T>, ex: JsonDataException): Exception {
        return ResponseError.SerializationException(
            message = ex.message,
            requestUrl = call.request().url,
            requestHeaders = call.request().headers
        )
    }
}