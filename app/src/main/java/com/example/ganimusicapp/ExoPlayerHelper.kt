package com.example.ganimusicapp

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer

/**
 * Singleton helper that manages a single ExoPlayer instance across activities.
 * - Use setPlaylist(...) to start a playlist.
 * - Exposes control helpers: play, pause, next, previous, seekTo, getPlayer().
 */
object ExoPlayerHelper {
    private var player: ExoPlayer? = null
    private var playlist: List<String> = listOf()

    /**
     * Prepare and start playlist. startIndex will be clamped to valid range.
     */
    fun setPlaylist(context: Context, urls: List<String>, startIndex: Int = 0) {
        if (player == null) {
            player = ExoPlayer.Builder(context.applicationContext).build()
        } else {
            player?.stop()
            player?.clearMediaItems()
        }

        playlist = urls.toList()

        // Add items
        for (url in playlist) {
            player?.addMediaItem(MediaItem.fromUri(url))
        }

        // Clamp startIndex
        val safeStart = when {
            playlist.isEmpty() -> 0
            startIndex < 0 -> 0
            startIndex >= playlist.size -> playlist.size - 1
            else -> startIndex
        }

        player?.seekTo(safeStart, 0)
        player?.prepare()
        player?.play()
    }

    fun getPlayer(): ExoPlayer? = player

    fun play() {
        player?.play()
    }

    fun pause() {
        player?.pause()
    }

    fun isPlaying(): Boolean = player?.isPlaying ?: false

    fun seekTo(positionMs: Long) {
        player?.seekTo(positionMs)
    }

    fun next() {
        player?.let { p ->
            if (p.hasNextMediaItem()) {
                p.seekToNextMediaItem()
                p.play()
            }
        }
    }

    fun previous() {
        player?.let { p ->
            // if previous exists, go previous; otherwise restart current
            if (p.hasPreviousMediaItem()) {
                p.seekToPreviousMediaItem()
                p.play()
            } else {
                p.seekTo(0)
                p.play()
            }
        }
    }

    fun release() {
        player?.release()
        player = null
    }
}
