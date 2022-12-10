package com.example.presentation.feed

import com.example.presentation.UiIntent
import com.example.presentation.UiState
import com.example.presentation.models.FilerFeedValueUiModel
import com.example.presentation.models.PostUiModel

class FeedContract {

    data class ShowFeedIntent(val filterValue: FilerFeedValueUiModel = FilerFeedValueUiModel.NONE) : UiIntent

    sealed class FeedViewState : UiState {
        object Loading : FeedViewState()
        data class Success(val postsList: List<PostUiModel>) : FeedViewState()
        object Error : FeedViewState()
    }

}