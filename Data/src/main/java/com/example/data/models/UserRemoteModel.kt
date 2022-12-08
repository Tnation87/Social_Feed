package com.example.data.models

import com.squareup.moshi.Json

data class UserRemoteModel(
    @Json(name = "fields") val userFields: UserFields?
): DataModel

data class UserFields(
    val username: StringValue?
)
