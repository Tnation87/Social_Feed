package com.example.presentation.feed

import androidx.lifecycle.viewModelScope
import com.example.presentation.BaseViewModel
import com.example.presentation.mappers.Mappers.postsMapper
import com.example.use_cases.GetPostsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class FeedViewModel @Inject constructor(val getPostsUseCase: GetPostsUseCase) :
    BaseViewModel<FeedContract.ShowFeedIntent, FeedContract.FeedViewState>() {
    override fun createInitialState(): FeedContract.FeedViewState {
        return FeedContract.FeedViewState.Idle
    }

    override fun handleIntent(intent: FeedContract.ShowFeedIntent) {
        getPosts()
    }

    /**
     * Gets all posts
     */
    private fun getPosts() {
        viewModelScope.launch(Dispatchers.IO) {
            // Set Loading
            setState { FeedContract.FeedViewState.Loading }

            val postsList = getPostsUseCase()
            if (postsList.isNotEmpty())
                setState {
                    FeedContract.FeedViewState.Success(postsList.map {
                        postsMapper.mapToUiModel(
                            it
                        )
                    })
                }
            else setState { FeedContract.FeedViewState.Error }
        }
    }
}