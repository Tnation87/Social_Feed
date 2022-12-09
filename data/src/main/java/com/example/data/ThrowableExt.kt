package com.example.data

import java.net.ConnectException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

fun Throwable.isNetworkError(): Boolean {
    when (this) {
        is SocketTimeoutException,
        is UnknownHostException,
        is ConnectException,
        is SocketException -> return true
    }
    return false
}