package com.example.streamvideoplayer

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.streamvideoplayer.model.mainVideoList
import com.example.streamvideoplayer.viewModel.VideoPlayerViewModel

@Composable
fun StreamerPlayer(
    viewModel: VideoPlayerViewModel,
    isPlaying: Boolean,
    onPlayerClosed: (isVideoPlaying: Boolean) -> Unit
) {
    Box(
        modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center
    ) {
        if (isPlaying) {
            AndroidView(modifier = Modifier.fillMaxWidth(), factory = { cont ->
                viewModel.playerViewBuilder(cont)
            })
            IconButton(
                onClick = {
                    onPlayerClosed(false)
                    viewModel.releasePlayer()
                }, modifier = Modifier.align(
                    Alignment.TopEnd
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Close, contentDescription = "", tint = Color.White
                )
            }
        } else {
            Image(
                painter = painterResource(id = R.drawable.loading), contentDescription = ""
            )
        }

    }


}