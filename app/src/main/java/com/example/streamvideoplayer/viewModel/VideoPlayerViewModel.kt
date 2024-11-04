package com.example.streamvideoplayer.viewModel

import android.app.Activity
import android.app.DownloadManager
import android.content.Context
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Environment
import androidx.annotation.OptIn
import androidx.lifecycle.ViewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.example.streamvideoplayer.model.VideoData

@OptIn(UnstableApi::class)
class VideoPlayerViewModel : ViewModel() {
    private var exoPlayer: ExoPlayer? = null
    var index: Int = 0
    var videoList: List<VideoData> = listOf()

    fun initializePlayer(context: Context) {
        exoPlayer = ExoPlayer.Builder(context).build()
    }

    fun releasePlayer() {
        exoPlayer?.playWhenReady = false
        exoPlayer?.release()
        exoPlayer = null
    }

    fun playVideo() {
        exoPlayer?.let { player ->
            player.stop()
            player.clearMediaItems()
            val mediaItem = MediaItem.fromUri(Uri.parse(videoList[index].videoUrl))
            player.setMediaItem(mediaItem)
            player.prepare()
            player.playWhenReady = true
            player.play()

            Log.d("VideoPlayer", "Playing video at index: $index, URL: ${videoList[index].videoUrl}")
        } ?: Log.e("VideoPlayer", "ExoPlayer is not initialized")
    }

    fun playerViewBuilder(context: Context): PlayerView {
        val activity = context as Activity
        val playerView = PlayerView(context).apply {
            player = exoPlayer
            controllerAutoShow = true
            keepScreenOn = true
            setFullscreenButtonClickListener { isFullScreen ->
                if (isFullScreen) {
                    activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_USER_LANDSCAPE
                } else {
                    activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_USER
                }
            }
        }
        return playerView
    }

    fun toggleBookmark() {
        if (videoList.isNotEmpty()) {
            videoList[index].isBookmarked = !videoList[index].isBookmarked
        }
    }

    // Function to download the current video
    fun downloadVideo(context: Context) {
        if (videoList.isNotEmpty()) {
            val videoUrl = videoList[index].videoUrl
            val request = DownloadManager.Request(Uri.parse(videoUrl))
            request.setTitle("Downloading Video")
            request.setDescription("Downloading ${videoList[index].thumbnail}")
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "video_${index}.mp4")

            val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            downloadManager.enqueue(request)

            Log.d("VideoPlayer", "Download started for video at index: $index, URL: $videoUrl")
        } else {
            Log.e("VideoPlayer", "Video list is empty")
        }
    }
}
