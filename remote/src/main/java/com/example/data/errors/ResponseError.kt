package com.example.data.errors

import okhttp3.Headers
import okhttp3.HttpUrl

sealed class ResponseError(private val errorMessage: String?) : RuntimeException(errorMessage) {

    open fun getLoggingMessage(): String? = errorMessage

    open fun toMetadata(): HashMap<String, Any?> = hashMapOf()

    data class SerializationException(
        override val message: String? = null,
        val requestUrl: HttpUrl,
        val requestHeaders: Headers
    ) : ResponseError(message) {

        override fun getLoggingMessage(): String {
            return "Serialization Error in $requestUrl"
        }

        override fun toMetadata(): HashMap<String, Any?> {
            val url = requestUrl.toString()
            val headers = requestHeaders.toString()
            return hashMapOf<String, Any?>().apply {
                if (!message.isNullOrEmpty()) {
                    this["error_message"] = message
                }
                if (url.isNotEmpty()) {
                    this["request_url"] = url
                }
                if (headers.isNotEmpty()) {
                    this["request_headers"] = headers
                }
            }
        }
    }

    /**
     * Connection Error
     */
    object NoConnection : ResponseError(errorMessage = null)
}
