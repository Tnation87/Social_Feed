package com.example.presentation

import com.example.presentation.feed.FeedContract
import com.example.presentation.feed.FeedViewModel
import com.example.use_cases.GetPostsUseCase
import com.example.use_cases.models.MediaType
import com.example.use_cases.models.PostModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import java.time.LocalDateTime

class FeedViewModelTest {
    private lateinit var viewModel: FeedViewModel

    @Mock
    private lateinit var getPostsUseCase: GetPostsUseCase

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(StandardTestDispatcher())
        viewModel = FeedViewModel(
            getPostsUseCase
        )
    }

    @Test
    fun successfulGetPostsScenario() = runBlocking {
        Mockito.`when`(getPostsUseCase()).thenReturn(listOf(PostModel(LocalDateTime.now(), MediaType.VIDEO, listOf(""),
            "", "", null, "")))

        viewModel.handleIntent(FeedContract.ShowFeedIntent())

        val testObserver = viewModel.uiState.take(2).toList()

        assert(testObserver[0] is FeedContract.FeedViewState.Loading)
        assert(testObserver[1] is FeedContract.FeedViewState.Success)
    }

    @Test
    fun failingGetPostsScenario() = runBlocking {
        Mockito.`when`(getPostsUseCase()).thenReturn(emptyList())

        viewModel.handleIntent(FeedContract.ShowFeedIntent())

        val testObserver = viewModel.uiState.take(2).toList()

        assert(testObserver[0] is FeedContract.FeedViewState.Loading)
        assert(testObserver[1] is FeedContract.FeedViewState.Error)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun cleanup() {
        Dispatchers.resetMain()
    }
}