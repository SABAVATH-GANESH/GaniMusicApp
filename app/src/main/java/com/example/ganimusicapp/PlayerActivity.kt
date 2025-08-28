package com.example.ganimusicapp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import androidx.media3.common.Player
import android.widget.Toast

class PlayerActivity : AppCompatActivity() {

    private lateinit var albumArt: ImageView
    private lateinit var songTitle: TextView
    private lateinit var songArtist: TextView
    private lateinit var seekBar: SeekBar
    private lateinit var tvCurrentTime: TextView
    private lateinit var tvTotalTime: TextView
    private lateinit var btnPrev: ImageButton
    private lateinit var btnPlayPause: ImageButton
    private lateinit var btnNext: ImageButton

    private val handler = Handler(Looper.getMainLooper())
    private val updateRunnable = object : Runnable {
        override fun run() {
            val player = ExoPlayerHelper.getPlayer()
            player?.let {
                val duration = it.duration
                val position = it.currentPosition

                if (duration > 0) {
                    seekBar.max = (duration / 1000).toInt()
                    tvTotalTime.text = formatTime(duration)
                }

                seekBar.progress = (position / 1000).toInt()
                tvCurrentTime.text = formatTime(position)
            }
            handler.postDelayed(this, 500)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        // Initialize views
        albumArt = findViewById(R.id.albumArt)
        songTitle = findViewById(R.id.songTitle)
        songArtist = findViewById(R.id.songArtist)
        seekBar = findViewById(R.id.seekBar)
        tvCurrentTime = findViewById(R.id.tvCurrentTime)
        tvTotalTime = findViewById(R.id.tvTotalTime)
        btnPrev = findViewById(R.id.btnPrev)
        btnPlayPause = findViewById(R.id.btnPlayPause)
        btnNext = findViewById(R.id.btnNext)

        // Get intent data
        val playlist = intent.getStringArrayListExtra("playlist")
        val startIndex = intent.getIntExtra("startIndex", 0)
        val titleFromIntent = intent.getStringExtra("title")
        val artistFromIntent = intent.getStringExtra("artist")
        val imageFromIntent = intent.getStringExtra("image")

        // Display metadata if available
        titleFromIntent?.let { songTitle.text = it }
        artistFromIntent?.let { songArtist.text = it }
        imageFromIntent?.let {
            Glide.with(this)
                .load(it)
                .placeholder(R.drawable.ic_music_placeholder)
                .into(albumArt)
        }

        // Set playlist if needed
        if (!playlist.isNullOrEmpty()) {
            ExoPlayerHelper.setPlaylist(this, playlist, startIndex)
        } else {
            val singleUrl = intent.getStringExtra("url")
            if (!singleUrl.isNullOrEmpty()) {
                ExoPlayerHelper.setPlaylist(this, listOf(singleUrl), 0) // ✅ fallback to single song
            } else {
                Toast.makeText(this, "No song URL provided", Toast.LENGTH_SHORT).show()
            }
        }


        val player = ExoPlayerHelper.getPlayer()

        // Listen for player events
        player?.addListener(object : Player.Listener {
            override fun onMediaItemTransition(mediaItem: androidx.media3.common.MediaItem?, reason: Int) {
                val index = player.currentMediaItemIndex
                songTitle.text = "Song ${index + 1}"
                // Optionally update artist/image if metadata available
            }

            override fun onIsPlayingChanged(isPlaying: Boolean) {
                btnPlayPause.setImageResource(
                    if (isPlaying) android.R.drawable.ic_media_pause
                    else android.R.drawable.ic_media_play
                )
            }

            override fun onPlaybackStateChanged(playbackState: Int) {
                if (playbackState == Player.STATE_ENDED) {
                    // Handle end of playback if needed
                }
            }
        })

        // Playback controls
        btnPlayPause.setOnClickListener {
            if (ExoPlayerHelper.isPlaying()) {
                ExoPlayerHelper.pause()
            } else {
                ExoPlayerHelper.play()
            }
        }

        btnNext.setOnClickListener { ExoPlayerHelper.next() }
        btnPrev.setOnClickListener { ExoPlayerHelper.previous() }

        // SeekBar interaction
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            var userSeeking = false

            override fun onProgressChanged(sb: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    ExoPlayerHelper.seekTo(progress * 1000L)
                }
            }

            override fun onStartTrackingTouch(sb: SeekBar?) {
                userSeeking = true
            }

            override fun onStopTrackingTouch(sb: SeekBar?) {
                userSeeking = false
            }
        })

        // Start UI updater
        handler.post(updateRunnable)
    }

    private fun formatTime(msLong: Long): String {
        if (msLong <= 0) return "0:00"
        val totalSec = (msLong / 1000).toInt()
        val min = totalSec / 60
        val sec = totalSec % 60
        return String.format("%d:%02d", min, sec)
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(updateRunnable)
        // Do not release ExoPlayer here to allow playback across activities
    }
}