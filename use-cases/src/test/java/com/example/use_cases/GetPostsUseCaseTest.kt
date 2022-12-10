package com.example.use_cases

import android.net.Uri
import com.example.data.models.ArrayValue
import com.example.data.models.StringValue
import com.example.data.models.TimeStampValue
import com.example.data.models.Values
import com.example.repos.feed.PostsRepo
import com.example.repos.models.*
import com.example.use_cases.models.FilterFeedValue
import com.example.use_cases.models.MediaType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class GetPostsUseCaseTest {
    private lateinit var useCase: GetPostsUseCase

    @Mock
    private lateinit var postsRepo: PostsRepo

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(StandardTestDispatcher())
        useCase = GetPostsUseCase(
            postsRepo
        )
    }

    @Test
    fun successfulGetPostsScenarioWithNoFilter() = runBlocking {
        `when`(postsRepo.getPosts()).thenReturn(
            bothMediaList
        )

        val result = useCase()

        assert(result.any { it.mediaType == MediaType.VIDEO })
        assert(result.any { it.mediaType == MediaType.IMAGE })
        assert(result[0].authorUserName == "Toka")
    }

    @Test
    fun successfulGetPostsScenarioWithImageFilter() = runBlocking {
        `when`(postsRepo.getPosts()).thenReturn(
            bothMediaList
        )

        val result = useCase(FilterFeedValue.IMAGES)

        assert(result.all { it.mediaType == MediaType.IMAGE })
        assert(result[0].authorUserName == "Toka")
    }

    @Test
    fun successfulGetPostsScenarioWithVideoFilter() = runBlocking {
        `when`(postsRepo.getPosts()).thenReturn(
            bothMediaList
        )

        val result = useCase(FilterFeedValue.VIDEOS)

        assert(result.all { it.mediaType == MediaType.VIDEO })
        assert(result[0].authorUserName == "Toka")
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun cleanup() {
        Dispatchers.resetMain()
    }

    companion object {
        private val videoList = FeedDataModel(
            users = listOf(
                UserItemModel(
                    "", UserFields(
                        StringValue("Toka")
                    )
                )
            ),
            posts = listOf(
                PostItemModel(
                    postFields = PostFields(
                        TimeStampValue("2022-12-10T17:52:08.666175Z"), StringValue("video"), ArrayValue(
                            Values(emptyList())
                        ), StringValue(""), StringValue(""), Uri.EMPTY, StringValue("")
                    )
                )
            )
        )

        private val imageList = videoList.copy(posts = videoList.posts.map {
            it?.copy(
                postFields = it.postFields?.copy(mediaType = StringValue("photo"))
            )
        })

        val bothMediaList =
            FeedDataModel(users = videoList.users, posts = videoList.posts + imageList.posts)
    }
}