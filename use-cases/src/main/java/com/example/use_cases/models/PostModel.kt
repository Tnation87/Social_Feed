package com.example.use_cases.models

import android.net.Uri
import java.time.LocalDateTime

data class PostModel(
    val createdAt: LocalDateTime?,
    val mediaType: MediaType?,
    val comments: List<String>?,
    val id: String,
    val caption: String?,
    val storageRef: Uri?,
    val authorUserName: String?
): Model

enum class MediaType{VIDEO, IMAGE}
