package com.example.streamvideoplayer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.streamvideoplayer.model.mainVideoList
import com.example.streamvideoplayer.ui.theme.StreamVideoPlayerTheme
import com.example.streamvideoplayer.viewModel.VideoPlayerViewModel


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContent {
            StreamVideoPlayerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    StreamingVideo()
                }
            }
        }
    }
}
//
//@Composable
//fun StreamingVideo() {
//    var isPlaying by remember {
//        mutableStateOf(false)
//    }
//    var videoItemIndex by remember {
//        mutableIntStateOf(0)
//    }
//    val viewModel: VideoPlayerViewModel = viewModel()
//    viewModel.videoList = mainVideoList
//    val context = LocalContext.current
//
//    Column {
//        StreamerPlayer(
//            viewModel = viewModel,
//            isPlaying = isPlaying,
//            onPlayerClosed = { isVideoPlaying ->
//                isPlaying = isVideoPlaying
//            })
//        LazyColumn(Modifier.padding(10.dp), content = {
//            itemsIndexed(items = mainVideoList) { index, item ->
//                Row(
//                    Modifier
//                        .fillMaxWidth()
//                        .clickable {
//                            if (videoItemIndex != index) isPlaying = false
//                            viewModel.index = index
//                            videoItemIndex = viewModel.index
//                        },
//                    horizontalArrangement = Arrangement.spacedBy(16.dp),
//                    verticalAlignment = Alignment.Bottom
//                ) {
//                    AsyncImage(model = item.thumbnail, contentDescription = "video thumbnail")
//                    Text(
//                        text = "Video ${index + 1}",
//                        Modifier
//                            .fillMaxSize()
//                            .weight(1f)
//                    )
//                }
//                Divider(
//                    Modifier
//                        .fillMaxWidth()
//                        .padding(vertical = 6.dp)
//                )
//            }
//        })
//        LaunchedEffect(key1 = videoItemIndex) {
//            isPlaying = true
//            viewModel.apply {
//                releasePlayer()
//                initializePlayer(context)
//                playVideo()
//            }
//        }
//    }


@Composable
fun StreamingVideo() {
    var isPlaying by remember { mutableStateOf(false) }
    var videoItemIndex by remember { mutableIntStateOf(0) }
    val viewModel: VideoPlayerViewModel = viewModel()
    viewModel.videoList = mainVideoList
    val context = LocalContext.current

    Column {
        StreamerPlayer(viewModel = viewModel,
            isPlaying = isPlaying,
            onPlayerClosed = { isVideoPlaying -> isPlaying = isVideoPlaying })

        LazyColumn(Modifier.padding(10.dp)) {
            itemsIndexed(items = mainVideoList) { index, item ->
                Row(
                    Modifier
                        .fillMaxWidth()
                        .clickable {
                            if (videoItemIndex != index) isPlaying = false
                            viewModel.index = index
                            videoItemIndex = viewModel.index
                        },
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.Bottom
                ) {
                    AsyncImage(model = item.thumbnail, contentDescription = "video thumbnail")
                    Text(text = "Video ${index + 1}", Modifier.weight(1f))

                    // Bookmark button
                    IconButton(onClick = { viewModel.toggleBookmark() }) {
                        Icon(
                            imageVector = if (item.isBookmarked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Bookmark",
                            modifier = Modifier.size(100.dp)

                        )
                    }

                    // Download button
                    IconButton(onClick = { viewModel.downloadVideo(context) }) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = "Download",
                            modifier = Modifier.size(100.dp)

                        )
                    }
                }
                Divider(Modifier.padding(vertical = 6.dp))
            }
        }

        LaunchedEffect(videoItemIndex) {
            isPlaying = true
            viewModel.apply {
                releasePlayer()
                initializePlayer(context)
                playVideo()
            }
        }
    }
}
