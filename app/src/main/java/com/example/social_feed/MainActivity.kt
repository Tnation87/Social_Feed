package com.example.social_feed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.ViewModel
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.social_feed.ui.theme.Social_FeedTheme
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ui.StyledPlayerView

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Social_FeedTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Feed()
                }
            }
        }
    }
}

@Composable
fun Feed() {
    val context = LocalContext.current
    LazyColumn() {
        MediaPost(exoPlayer = remember(context) {
            ExoPlayer.Builder(context).build()
        })
    }
}

@Composable
fun MediaPost(viewModel: ViewModel, exoPlayer: ExoPlayer, index: Int) {
    Column {
        Text(text = "username")
        Image(painter =, contentDescription = null)
        VideoScreen(viewModel = viewModel, exoPlayer = exoPlayer, video = video, index = index)
    }
}

@Composable
fun VideoScreen(viewModel: ViewModel, exoPlayer: ExoPlayer, video: VideoItem, index: Int) {
    val playingItemIndex = // get index of currently playing item from viewModel
    val isPlaying = index == playingItemIndex

    var isPlayerUiVisible by remember { mutableStateOf(false) }
    val isPlayButtonVisible = if (isPlayerUiVisible) true else !isPlaying

    Box {
        if (isPlaying) {
            VideoPlayer(exoPlayer) { uiVisible ->
                isPlayerUiVisible = when {
                    isPlayerUiVisible -> uiVisible
                    else -> true
                }
            }
        } else {
            VideoThumbnail(video.thumbnail)
        }
        if (isPlayButtonVisible) {
            Icon(
                painter = painterResource(if (isPlaying) R.drawable.ic_baseline_pause_24 else R.drawable.ic_baseline_play_arrow_24),
                contentDescription = null,
                modifier = Modifier.clickable {
                    // emit intent of user wanting to play video giving index of current item and position of playing
                })
        }
    }
}

@Composable
fun VideoPlayer(
    exoPlayer: ExoPlayer,
    onControllerVisibilityChanged: (uiVisible: Boolean) -> Unit
) {
    val context = LocalContext.current
    val playerView = remember {
        val layout = LayoutInflater.from(context).inflate(R.layout.video_player, null, false)
        val playerView = layout.findViewById(R.id.playerView) as StyledPlayerView
        playerView.apply {
            val listener: StyledPlayerView.ControllerVisibilityListener =
                StyledPlayerView.ControllerVisibilityListener {
                    onControllerVisibilityChanged(
                        it == View.VISIBLE
                    )
                }
            setControllerVisibilityListener(listener)
            player = exoPlayer
        }
    }

    AndroidView({ playerView })
}

@Composable
fun VideoThumbnail(url: String) {
    Image(
        painter = rememberAsyncImagePainter(
            ImageRequest.Builder(LocalContext.current).data(data = url)
                .apply(block = fun ImageRequest.Builder.() {
                    crossfade(true)
                    size(512, 512)
                }).build()
        ),
        contentDescription = null,
        modifier = Modifier
            .fillMaxWidth()
            .size(256.dp),
        contentScale = ContentScale.Crop
    )
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Social_FeedTheme {
        Feed()
    }
}