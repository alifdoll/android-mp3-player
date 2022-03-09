package com.alifdoll.musicplayer

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alifdoll.musicplayer.databinding.ItemMusicBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class MusicRecyclerAdapter(val musics: ArrayList<Music>) : RecyclerView.Adapter<MusicRecyclerAdapter.MusicViewHolder>(){

//    private val musics = ArrayList<Music>()
//
//    fun setMusic(musicList: List<Music>) {
//        if (musicList.isNullOrEmpty()) return
//        musics.clear()
//        musics.addAll(musicList)
//    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MusicRecyclerAdapter.MusicViewHolder {
        val binding = ItemMusicBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MusicViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MusicRecyclerAdapter.MusicViewHolder, position: Int) {
        val music = musics[position]
        holder.bind(music)
    }

    override fun getItemCount() = musics.size

    inner class MusicViewHolder(val binding: ItemMusicBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(music: Music) {
            binding.apply {
                musicTitle.text = music.title
                musicArtist.text = music.artist

//                Glide
//                    .with(itemView.context)
//                    .load(music.image)
//                    .into(musicPoster)
            }
        }

    }
}