package com.example.streamvideoplayer.model

data class VideoData(
    val thumbnail: String,
    val videoUrl: String,
    var isBookmarked: Boolean = false // New property for bookmarking
)
