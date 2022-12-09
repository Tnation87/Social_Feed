package com.example.use_cases.models

data class PostItemModel(
    val createdAt: String?,
    val mediaType: String?,
    val comments: List<String>?,
    val id: String?,
    val caption: String?,
    val storageRef: String?,
    val authorUserName: String?
): ItemModel
