package com.example.use_cases.models

data class PostItemModel(
    val createdAt: String?,
    val mediaType: ItemMediaType?,
    val comments: List<String>?,
    val id: String?,
    val caption: String?,
    val storageRef: String?,
    val authorUserName: String?
): ItemModel

enum class ItemMediaType{VIDEO, IMAGE}
