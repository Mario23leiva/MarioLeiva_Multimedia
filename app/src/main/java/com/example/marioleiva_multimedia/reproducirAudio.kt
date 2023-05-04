package com.example.marioleiva_multimedia

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.widget.MediaController
import com.example.marioleiva_multimedia.databinding.ActivityReproducirAudioBinding
import java.io.File
import java.io.IOException

class reproducirAudio : AppCompatActivity(), MediaController.MediaPlayerControl {
    private lateinit var binding: ActivityReproducirAudioBinding
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var mediaController: MediaController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()

        binding = ActivityReproducirAudioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val filePath = intent.getStringExtra("selected_file_path") ?: ""
        mediaPlayer = MediaPlayer().apply {
            setDataSource(filePath)
            prepare()
        }

        mediaController = MediaController(this)
        mediaController.setMediaPlayer(this)
        mediaController.setAnchorView(binding.audioView)

        binding.audioView.setMediaController(mediaController)
        binding.audioView.setVideoURI(Uri.parse(filePath))
        binding.audioView.requestFocus()
        binding.audioView.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }

    override fun isPlaying(): Boolean {
        return mediaPlayer.isPlaying
    }

    override fun canPause(): Boolean {
        return true
    }

    override fun canSeekBackward(): Boolean {
        return true
    }

    override fun canSeekForward(): Boolean {
        return true
    }

    override fun getDuration(): Int {
        return mediaPlayer.duration
    }

    override fun getCurrentPosition(): Int {
        return mediaPlayer.currentPosition
    }

    override fun pause() {
        mediaPlayer.pause()
    }

    override fun seekTo(pos: Int) {
        mediaPlayer.seekTo(pos)
    }

    override fun start() {
        mediaPlayer.start()
    }

    override fun getBufferPercentage(): Int {
        return 0
    }

    override fun getAudioSessionId(): Int {
        return mediaPlayer.audioSessionId
    }
}