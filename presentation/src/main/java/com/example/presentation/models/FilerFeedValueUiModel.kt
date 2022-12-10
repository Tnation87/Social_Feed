package com.example.presentation.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class FilerFeedValueUiModel(val textVal: String): UiModel, Parcelable {
    VIDEOS("Video posts"), IMAGES("Image posts"), NONE(
        ""
    )
}