package com.example.social_feed.features.feed

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import coil.compose.AsyncImage
import com.example.presentation.feed.FeedContract
import com.example.presentation.feed.FeedViewModel
import com.example.presentation.models.PostUiModel
import com.example.presentation.models.UiMediaType
import com.example.social_feed.R
import com.example.social_feed.ui.theme.Social_FeedTheme
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ui.StyledPlayerView
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var viewModel: FeedViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setIntent(FeedContract.ShowFeedIntent)

        setContent {
            Social_FeedTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Feed(viewModel)
                }
            }
        }
    }
}

@Composable
fun Feed(viewModel: FeedViewModel) {
    val state by viewModel.uiState.collectAsState()

    when (state) {
        is FeedContract.FeedViewState.Loading -> ShimmeringList()
        is FeedContract.FeedViewState.Success -> Posts(
            (state as FeedContract.FeedViewState.Success).postsList
        )
        is FeedContract.FeedViewState.Error -> Error()
    }
}

@Composable
fun ShimmeringList() {

}

@Composable
fun Error() {

}

@Composable
fun Posts(postsList: List<PostUiModel>) {
    val context = LocalContext.current
    LazyColumn {
        itemsIndexed(items = postsList, key = { _, post -> post.id }) { index, post ->
            MediaPost(exoPlayer = remember(context) {
                ExoPlayer.Builder(context).build()
            }, index = index, post = post)
        }
    }
}

@Composable
fun MediaPost(exoPlayer: ExoPlayer, index: Int, post: PostUiModel) {
    Column {
        post.authorUserName?.let { Text(text = it) }

        val mediaType = post.mediaType
        val storageRef = post.storageRef
        if (mediaType != null && storageRef != null) {
            val storage = Firebase.storage.getReferenceFromUrl("gs://peach-assessment.appspot.com/$storageRef")
            when (mediaType) {
                UiMediaType.IMAGE -> {
                    AsyncImage(model = storage.downloadUrl, contentDescription = null)
                }
                UiMediaType.VIDEO -> VideoScreen(
                    exoPlayer = exoPlayer,
                    video = post.storageRef,
                    index = index
                )
            }
        }
        post.caption?.let { if (it.trim().isNotEmpty()) Text(text = it) }
        post.createdAt?.let { Text(text = it.toString()) }
        post.comments?.let { if (it.isNotEmpty())
            LazyColumn {
            }
        }
    }
}

@Composable
fun VideoScreen(exoPlayer: ExoPlayer, video: String?, index: Int) {
    Box {
        VideoPlayer(exoPlayer)
    }
}

@Composable
fun VideoPlayer(
    exoPlayer: ExoPlayer
) {
    val context = LocalContext.current
    val playerView = remember {
        val layout = LayoutInflater.from(context).inflate(R.layout.video_player, null, false)
        val playerView = layout.findViewById(R.id.playerView) as StyledPlayerView
        playerView.apply {
            player = exoPlayer
        }
    }

    AndroidView({ playerView })
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Social_FeedTheme {
    }
}