package com.example.presentation.feed

import com.example.presentation.UiIntent
import com.example.presentation.UiState
import com.example.presentation.models.PostUiModel

class FeedContract {

    object ShowFeedIntent : UiIntent

    sealed class FeedViewState: UiState {
        object Idle : FeedViewState()
        object Loading : FeedViewState()
        data class Success(val postsList : List<PostUiModel>) : FeedViewState()
        object Error: FeedViewState()
    }

}