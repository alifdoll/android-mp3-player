package com.alifdoll.musicplayer

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.LinearLayout
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.alifdoll.musicplayer.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var mediaPlayer: MediaPlayer? = null

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

    @SuppressLint("Range")
    private fun loadMusic() {
        val listsMusic =  ArrayList<Music>()
        var mediaUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        var select = MediaStore.Audio.Media.IS_MUSIC + "!=0"

        var rs = contentResolver.query(mediaUri, null, select, null, null)
        if (rs != null) {
            while (rs!!.moveToNext()) {
                var uri = rs!!.getString(rs!!.getColumnIndex(MediaStore.Audio.Media.DATA))
                var author = rs.getString(rs.getColumnIndex(MediaStore.Audio.Media.ARTIST))
                var title = rs.getString(rs.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME))

                var music = Music(title, author, uri)
                listsMusic.add(music)
            }
        }
        binding.apply {
            rvMusic.apply {
                layoutManager = LinearLayoutManager(this@MainActivity)
                adapter = MusicRecyclerAdapter(listsMusic)
                setHasFixedSize(true)
            }
        }
    }
}