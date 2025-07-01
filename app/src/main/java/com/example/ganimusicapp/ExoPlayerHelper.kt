//package com.example.ganimusicapp
//
//import android.content.Context
//import androidx.media3.exoplayer.ExoPlayer
//import androidx.media3.common.MediaItem
//
//object ExoPlayerHelper {
//    private var player: ExoPlayer? = null
//
//    fun play(context: Context, url: String) {
//        if (player == null) {
//            player = ExoPlayer.Builder(context).build()
//        } else {
//            player?.stop()
//        }
//
//        val mediaItem = MediaItem.fromUri(url)
//        player?.setMediaItem(mediaItem)
//        player?.prepare()
//        player?.play()
//    }
//
//    fun stop() {
//        player?.stop()
//    }
//
//    fun release() {
//        player?.release()
//        player = null
//    }
//}
package com.example.ganimusicapp

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer

object ExoPlayerHelper {
    private var player: ExoPlayer? = null

    fun play(context: Context, url: String) {
        if (player == null) {
            player = ExoPlayer.Builder(context).build()
        } else {
            player?.stop()
            player?.clearMediaItems()
        }

        val mediaItem = MediaItem.fromUri(url)
        player?.setMediaItem(mediaItem)
        player?.prepare()
        player?.play()
    }

    fun stop() {
        player?.stop()
    }

    fun release() {
        player?.release()
        player = null
    }
}
