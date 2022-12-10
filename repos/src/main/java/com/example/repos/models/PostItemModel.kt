package com.example.repos.models

import android.net.Uri
import com.example.data.models.ArrayValue
import com.example.data.models.StringValue
import com.example.data.models.TimeStampValue

data class PostItemModel(val postFields: PostFields?) : ItemModel

data class PostFields(
    val createdAt: TimeStampValue?,
    val mediaType: StringValue?,
    val comments: ArrayValue?,
    val id: StringValue,
    val caption: StringValue?,
    val storageRef: Uri?,
    val authorID: StringValue?
)