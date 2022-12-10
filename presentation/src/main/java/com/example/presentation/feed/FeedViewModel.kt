package com.example.presentation.feed

import androidx.lifecycle.viewModelScope
import com.example.presentation.BaseViewModel
import com.example.presentation.mappers.Mappers.postsMapper
import com.example.use_cases.GetPostsUseCase
import com.example.use_cases.models.MediaType
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
            val postsList = getPostsUseCase()
            val filteredPosts = postsList.filter {
                val mediaType = it.mediaType
                when (intent.filterValue) {
                    FilerFeedValue.VIDEOS -> mediaType == MediaType.VIDEO
                    FilerFeedValue.IMAGES
                    -> mediaType == MediaType.IMAGE
                    FilerFeedValue.NONE -> true
                }
            }

            if (filteredPosts.isNotEmpty()) {
                setState {
                    FeedContract.FeedViewState.Success(filteredPosts.map {
                        postsMapper.mapToUiModel(
                            it
                        )
                    })
                }
            } else setState { FeedContract.FeedViewState.Error }
        }
    }
}