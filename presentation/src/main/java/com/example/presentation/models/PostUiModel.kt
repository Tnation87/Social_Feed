package com.example.presentation.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime

@Parcelize
data class PostUiModel(
    val createdAt: LocalDateTime?,
    val mediaType: UiMediaType?,
    val comments: List<String>?,
    val id: String?,
    val caption: String?,
    val storageRef: String?,
    val authorUserName: String?
) : UiModel, Parcelable

enum class UiMediaType{VIDEO, IMAGE}
