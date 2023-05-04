package com.example.marioleiva_multimedia

import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.MediaController
import com.example.marioleiva_multimedia.databinding.ActivityVerVideosBinding

class VerVideos : AppCompatActivity() {
    private lateinit var binding: ActivityVerVideosBinding
    private lateinit var mediaController: MediaController
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var videoUri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()

        val nombreRuta = intent.getStringExtra("nombreRuta")
        videoUri = Uri.parse(nombreRuta)

        binding = ActivityVerVideosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mediaController = MediaController(this)
        mediaPlayer = MediaPlayer.create(this, videoUri)

        binding.videoView.setMediaController(mediaController)
        binding.videoView.setVideoURI(videoUri)

        binding.videoView.setOnPreparedListener { mediaPlayer ->
            mediaPlayer.start()
            mediaController.setAnchorView(binding.videoView)
        }

        binding.videoView.setOnCompletionListener { mediaPlayer ->
            mediaPlayer.seekTo(0)
            binding.videoView.start()
        }

        binding.closeButton.setOnClickListener {
            mediaPlayer.stop()
            mediaPlayer.release()
            finish()
        }

        binding.fileNameTextView.text = nombreRuta!!.substring(nombreRuta.lastIndexOf("/") + 1)
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }
}