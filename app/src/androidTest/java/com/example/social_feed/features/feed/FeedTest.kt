package com.example.social_feed.features.feed

import androidx.activity.compose.setContent
import androidx.annotation.StringRes
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.example.presentation.feed.FeedContract
import com.example.presentation.feed.FeedViewModel
import com.example.presentation.models.FilerFeedValueUiModel
import com.example.social_feed.extensions.waitUntilDoesNotExist
import com.example.social_feed.extensions.waitUntilExists
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import com.example.social_feed.R

class FeedTest {

    private lateinit var viewModel: FeedViewModel

    @get:Rule
    val rule = createAndroidComposeRule<MainActivity>()

    private lateinit var mainFilterBtn: SemanticsMatcher
    private lateinit var dialogFilterBtn: SemanticsMatcher
    private lateinit var loadingView: SemanticsMatcher
    private lateinit var errorView: SemanticsMatcher
    private lateinit var feedView: SemanticsMatcher
    private lateinit var dialog: SemanticsMatcher

    private fun nodeWithText(@StringRes resId: Int) =
        hasText(rule.activity.getString(resId))

    private fun nodeWithTagText(@StringRes resId: Int) =
        hasTestTag(rule.activity.getString(resId))

    @Before
    fun setup() {
        viewModel = rule.activity.viewModel
        viewModel.setIntent(FeedContract.ShowFeedIntent())
        rule.activity.setContent { Feed(viewModel) }

        mainFilterBtn = nodeWithText(R.string.filter_title)
        dialogFilterBtn = nodeWithText(R.string.filter_btn_txt)
        loadingView = nodeWithTagText(R.string.test_tag_loading)
        errorView = nodeWithTagText(R.string.test_tag_error)
        feedView = nodeWithTagText(R.string.test_tag_feed)
        dialog = nodeWithTagText(R.string.test_tag_dialog)
    }

    @Test
    fun filterByVideos_ShouldLoadThenSucceedOrFail() {
        setup()

        filterFeed(FilerFeedValueUiModel.VIDEOS)

        assertRequestFinishes()
    }

    @Test
    fun filterByImages_ShouldLoadThenSucceedOrFail() {
        setup()

        filterFeed(FilerFeedValueUiModel.IMAGES)

        assertRequestFinishes()
    }

    @Test
    fun filterByNothing_ShouldLoadThenSucceedOrFail() {
        setup()

        filterFeed()

        assertRequestFinishes()
    }

    private fun filterFeed(filerFeedValueUiModel: FilerFeedValueUiModel? = null) {
        rule.waitUntilExists(mainFilterBtn)
        rule.onNode(mainFilterBtn).performClick()

        rule.waitUntilExists(dialog)
        if (filerFeedValueUiModel != null)
            rule.onNodeWithText(filerFeedValueUiModel.textVal).performClick()
        rule.onNode(dialogFilterBtn).performClick()
    }

    private fun assertRequestFinishes() {
        rule.waitUntilExists(loadingView)

        rule.waitUntilDoesNotExist(loadingView)
        rule.onNode(
            feedView or errorView
        ).assertIsDisplayed()
    }
}