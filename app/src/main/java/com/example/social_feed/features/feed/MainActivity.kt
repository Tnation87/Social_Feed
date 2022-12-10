package com.example.social_feed.features.feed

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.presentation.feed.FeedContract
import com.example.presentation.feed.FeedViewModel
import com.example.presentation.feed.FilerFeedValue
import com.example.presentation.models.PostUiModel
import com.example.presentation.models.UiMediaType
import com.example.social_feed.ui.theme.Social_FeedTheme
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ui.StyledPlayerView
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
        viewModel.setIntent(FeedContract.ShowFeedIntent())

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
            viewModel,
            (state as FeedContract.FeedViewState.Success).postsList
        )
        is FeedContract.FeedViewState.Error -> Error()
    }
}

@Composable
fun LoadingList() {
    StateBox {
        CircularProgressIndicator(modifier = Modifier.size(100.dp))
    }
}

@Composable
fun Error() {
    StateBox {
        Text(
            text = "No posts found :(",
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun StateBox(content: @Composable BoxScope.() -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp), contentAlignment = Alignment.Center,
        content = content
    )
}

@Composable
fun Posts(viewModel: FeedViewModel, postsList: List<PostUiModel>) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            var showDialog by remember { mutableStateOf(false) }

            Icon(
                modifier = Modifier.clickable { showDialog = true },
                imageVector = Icons.Default.FilterAlt,
                contentDescription = null
            )

            Text(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .clickable { showDialog = true },
                text = "Filter feed"
            )

            if (showDialog)
                FilterDialogView(viewModel = viewModel) {
                    showDialog = false
                }
        }

        LazyColumn(contentPadding = PaddingValues(vertical = 16.dp)) {
            items(postsList) { post ->
                MediaPost(post)
            }
        }
    }
}

@Composable
fun FilterDialogView(viewModel: FeedViewModel, onDismiss: () -> Unit) {
    Dialog(onDismissRequest = { onDismiss() }) {
        Column(
            Modifier
                .padding(8.dp)
                .background(color = MaterialTheme.colors.background),
        ) {

            Text(
                text = "Filter feed:",
                modifier = Modifier.padding(8.dp),
                fontSize = 16.sp
            )

            var selectedValue by remember { mutableStateOf(FilerFeedValue.NONE) }

            val isSelectedItem: (FilerFeedValue) -> Boolean = { selectedValue == it }
            val onChangeState: (FilerFeedValue) -> Unit = { selectedValue = it }

            val items = listOf(FilerFeedValue.VIDEOS, FilerFeedValue.IMAGES)
            Column(Modifier.padding(8.dp)) {
                items.forEach { item ->
                    Row(
                        modifier = Modifier
                            .selectable(
                                selected = isSelectedItem(item),
                                onClick = { onChangeState(item) },
                                role = Role.RadioButton
                            )
                            .padding(8.dp)
                    ) {
                        RadioButton(
                            selected = isSelectedItem(item),
                            onClick = null
                        )
                        Text(
                            text = item.textVal,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            OutlinedButton(
                onClick = { onDismiss() },
                Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text(text = "Cancel")
            }


            Button(
                onClick = {
                    viewModel.setIntent(FeedContract.ShowFeedIntent(selectedValue))
                    onDismiss()
                },
                Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text(text = "Filter")
            }

        }
    }
}

@Composable
fun MediaPost(post: PostUiModel) {
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
                    video = post.storageRef
                )
            }
        }

        post.caption?.let {
            if (it.trim().isNotEmpty()) Text(
                text = it,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        post.comments?.let {
            if (it.isNotEmpty()) {
                var showComments by remember { mutableStateOf(false) }

                if (!showComments)
                    CommentsHeader(icon = Icons.Default.ArrowDownward) {
                        showComments = true
                    }
                else {
                    Column {
                        CommentsHeader(icon = Icons.Default.ArrowUpward) {
                            showComments = false
                        }

                        CommentsSection(comments = it)
                    }
                }

            }
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

        Separator()
    }
}

@Composable
fun CommentsHeader(icon: ImageVector, onClick: () -> Unit) {
    Row(modifier = Modifier
        .padding(vertical = 8.dp)
        .clickable { onClick() }) {
        Icon(
            modifier = Modifier.padding(horizontal = 8.dp),
            imageVector = icon,
            contentDescription = null
        )

        Text(
            text = "Comments",
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun CommentsSection(comments: List<String>) {
    comments.forEach { comment ->
        Text(modifier = Modifier.padding(vertical = 8.dp), text = comment)
        Separator()
    }
}

@Composable
fun Separator() {
    Divider(modifier = Modifier.padding(vertical = 16.dp))
}

@Composable
fun VideoScreen(video: Uri?) {
    val context = LocalContext.current

    val exoPlayer = ExoPlayer.Builder(LocalContext.current)
        .build()
        .also { exoPlayer ->
            val mediaItem = MediaItem.Builder()
                .setUri(video)
                .build()
            exoPlayer.setMediaItem(mediaItem)
            exoPlayer.prepare()
        }

    DisposableEffect(
        AndroidView(modifier = Modifier.padding(vertical = 8.dp), factory = {
            StyledPlayerView(context).apply {
                player = exoPlayer
            }
        })
    ) {
        onDispose { exoPlayer.release() }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Social_FeedTheme {
        Error()
    }
}