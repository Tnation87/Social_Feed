package com.example.data.models

import com.squareup.moshi.Json

data class PostRemoteModel(@Json(name = "fields") val postFields: PostFields?) : RemoteModel

data class PostFields(
    val createdAt: TimeStampValue?,
    val mediaType: StringValue?,
    val comments: ArrayValue?,
    val id: StringValue,
    val caption: StringValue?,
    val storageRef: StringValue?,
    val authorID: StringValue?
)