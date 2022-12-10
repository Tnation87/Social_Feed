package com.example.data.models

import com.squareup.moshi.Json

data class UserRemoteModel(
    @Json(name = "name") val path: String?,
    @Json(name = "fields") val userFields: UserFields?
): RemoteModel

data class UserFields(
    val username: StringValue?
)
