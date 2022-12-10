package com.example.social_feed.features.feed

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.presentation.feed.FeedContract
import com.example.presentation.feed.FeedViewModel
import com.example.presentation.models.PostUiModel
import com.example.presentation.models.UiMediaType
import com.example.social_feed.ui.theme.Social_FeedTheme
import com.google.android.exoplayer2.ExoPlayer
import dagger.hilt.android.AndroidEntryPoint
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
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
        is FeedContract.FeedViewState.Loading -> LoadingList()
        is FeedContract.FeedViewState.Success -> Posts(
            (state as FeedContract.FeedViewState.Success).postsList
        )
        is FeedContract.FeedViewState.Error -> Error()
    }
}

@Composable
fun LoadingList() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp), contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(modifier = Modifier.size(100.dp))
    }
}

@Composable
fun Error() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp), contentAlignment = Alignment.Center
    ) {
        Text(
            text = "We're facing issues getting posts, please try again later :(",
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun Posts(postsList: List<PostUiModel>) {
    val context = LocalContext.current
    LazyColumn(contentPadding = PaddingValues(vertical = 16.dp)) {
        itemsIndexed(items = postsList, key = { _, post -> post.id }) { index, post ->
            MediaPost(exoPlayer = remember(context) {
                ExoPlayer.Builder(context).build()
            }, index = index, post = post)
        }
    }
}

@Composable
fun MediaPost(exoPlayer: ExoPlayer, index: Int, post: PostUiModel) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {

        post.authorUserName?.let { Text(text = it, fontWeight = FontWeight.Bold) }

        val mediaType = post.mediaType
        val storageRef = post.storageRef
        if (mediaType != null && storageRef != null) {
            when (mediaType) {
                UiMediaType.IMAGE -> {
                    AsyncImage(
                        modifier = Modifier.padding(vertical = 8.dp),
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(storageRef)
                            .crossfade(true)
                            .build(), contentDescription = null
                    )
                }
                UiMediaType.VIDEO -> VideoScreen(
                    exoPlayer = exoPlayer,
                    video = post.storageRef,
                    index = index
                )
            }
        }

        post.caption?.let {
            if (it.trim().isNotEmpty()) Text(
                text = it,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        post.createdAt?.let {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                text = it.format(
                    DateTimeFormatter.ofLocalizedDateTime(
                        FormatStyle.SHORT
                    )
                ),
                textAlign = TextAlign.End,
                fontSize = 12.sp
            )
        }

//        post.comments?.let { if (it.isNotEmpty())
//            LazyColumn {
//            }
//        }

        Divider(modifier = Modifier.padding(vertical = 16.dp))
    }
}

@Composable
fun VideoScreen(exoPlayer: ExoPlayer, video: Uri?, index: Int) {
    Box {
        VideoPlayer(exoPlayer)
    }
}

@Composable
fun VideoPlayer(
    exoPlayer: ExoPlayer
) {
    val context = LocalContext.current
//    val playerView = remember {
//        val layout = LayoutInflater.from(context).inflate(R.layout.video_player, null, false)
////        val playerView = layout.findViewById(R.id.playerView) as StyledPlayerView
////        playerView.apply {
////            player = exoPlayer
////        }
//    }

    //AndroidView({ playerView })
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Social_FeedTheme {
        Error()
    }
}