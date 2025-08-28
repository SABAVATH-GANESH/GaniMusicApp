package com.example.ganimusicapp

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import android.util.Log

class MusicService : Service() {

    companion object {
        var mediaPlayer: MediaPlayer? = null
        var songList: ArrayList<String> = arrayListOf()
        var currentIndex: Int = 0
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        try {
            when (intent?.action) {
                "PLAY" -> {
                    currentIndex = intent.getIntExtra("index", 0)
                    songList = intent.getStringArrayListExtra("songs") ?: arrayListOf()
                    playSong(currentIndex)
                }
                "NEXT" -> playNext()
                "PREV" -> playPrev()
                "PAUSE" -> pauseSong()
                else -> { /* no-op */ }
            }
        } catch (ex: Exception) {
            Log.e("MusicService", "Error in onStartCommand", ex)
        }
        return START_STICKY
    }

    private fun playSong(index: Int) {
        if (songList.isEmpty()) return
        mediaPlayer?.release()
        mediaPlayer = MediaPlayer().apply {
            setDataSource(songList[index])
            prepare()
            start()
        }
    }

    private fun playNext() {
        if (currentIndex < songList.size - 1) {
            currentIndex++
            playSong(currentIndex)
        }
    }

    private fun playPrev() {
        if (currentIndex > 0) {
            currentIndex--
            playSong(currentIndex)
        }
    }

    private fun pauseSong() {
        mediaPlayer?.pause()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
