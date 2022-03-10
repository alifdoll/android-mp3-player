package com.alifdoll.musicplayer

import android.net.Uri

data class Music(
    val title: String,
    val artist: String,
    val uri: String,
    val image: Long,
    var imageURI: Uri? = null
)
