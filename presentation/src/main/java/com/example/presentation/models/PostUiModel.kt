package com.example.presentation.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PostUiModel(
    val createdAt: String?,
    val mediaType: String?,
    val comments: List<String>?,
    val id: String?,
    val caption: String?,
    val storageRef: String?,
    val authorUserName: String?
) : UiModel, Parcelable
