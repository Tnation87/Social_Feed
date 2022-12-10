package com.example.presentation.feed

import androidx.lifecycle.viewModelScope
import com.example.presentation.BaseViewModel
import com.example.presentation.mappers.Mappers.filterFeedValueMapper
import com.example.presentation.mappers.Mappers.postsMapper
import com.example.use_cases.GetPostsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class FeedViewModel @Inject constructor(val getPostsUseCase: GetPostsUseCase) :
    BaseViewModel<FeedContract.ShowFeedIntent, FeedContract.FeedViewState>() {
    override fun createInitialState(): FeedContract.FeedViewState {
        return FeedContract.FeedViewState.Loading
    }

    override fun handleIntent(intent: FeedContract.ShowFeedIntent) {
        getPosts(intent)
    }

    /**
     * Gets all posts
     */
    private fun getPosts(intent: FeedContract.ShowFeedIntent) {
        setState { FeedContract.FeedViewState.Loading }
        viewModelScope.launch(Dispatchers.IO) {
            val postsList = getPostsUseCase(filterFeedValueMapper.mapFromUiModel(intent.filterValue))

            if (postsList.isNotEmpty()) {
                setState {
                    FeedContract.FeedViewState.Success(postsList.map {
                        postsMapper.mapToUiModel(
                            it
                        )
                    })
                }
            } else setState { FeedContract.FeedViewState.Error }
        }
    }
}