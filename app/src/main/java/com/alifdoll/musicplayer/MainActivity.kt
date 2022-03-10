package com.alifdoll.musicplayer

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Debug
import android.provider.MediaStore
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.alifdoll.musicplayer.databinding.ActivityMainBinding
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var listMusic = ArrayList<Music>()
    private var mediaPlayer: MediaPlayer? = null
    private var isPlayed = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
            !=
            PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 111)
        } else {

            loadMusic()
        }

        if (listMusic.size > 0) {
            currentPlay()
        }

        mediaPlayer = MediaPlayer()

        binding.playButton.setOnClickListener {
            if (isPlayed) {
                mediaPlayer!!.stop()
                isPlayed = false
                binding.playButton.setBackgroundResource(R.drawable.ic_baseline_play_arrow_24)

            } else {
                isPlayed = true
                val music = listMusic[0]
                try {
                    mediaPlayer!!.setDataSource(music.uri)
                    mediaPlayer!!.prepare()
                    mediaPlayer!!.start()
                    binding.playButton.setBackgroundResource(R.drawable.ic_baseline_pause_24)
                } catch (e: Exception) {
                    throw e
                }
            }

        }

    }

    private fun currentPlay() {
        val music = listMusic[0]

        binding.musicPoster.setImageURI(music.imageURI)
        binding.musicTitle.text = music.title
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 111 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            loadMusic()
        }
    }

    @SuppressLint("Range", "Recycle")
    private fun loadMusic() {
        val mediaUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val select = MediaStore.Audio.Media.IS_MUSIC + "!=0"

        val rs = contentResolver.query(mediaUri, null, select, null, null)


        if (rs != null) {
            while (rs.moveToNext()) {
                val uri = rs.getString(rs.getColumnIndex(MediaStore.Audio.Media.DATA))
                val author = rs.getString(rs.getColumnIndex(MediaStore.Audio.Media.ARTIST))
                val title = rs.getString(rs.getColumnIndex(MediaStore.Audio.Media.TITLE))
                val albumId = rs.getLong(rs.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ID))

                val uriImage = Uri.Builder()
                uriImage
                    .scheme("content")
                    .authority("media")
                    .appendPath("external")
                    .appendPath("audio")
                    .appendPath("albumart")
                    .appendPath(albumId.toString())


                val music = Music(title, author, uri, uriImage.build())
                listMusic.add(music)


            }
        }

        binding.apply {
            rvMusic.apply {
                layoutManager = LinearLayoutManager(this@MainActivity)
                adapter = MusicRecyclerAdapter(listMusic)
                setHasFixedSize(true)
            }
        }
    }
}