//package com.example.ganimusicapp
//
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.TextView
//import androidx.recyclerview.widget.RecyclerView
//
//class SongAdapter(
//    private val songs: List<Song>,
//    private val onSongClick: (Song) -> Unit
//) : RecyclerView.Adapter<SongAdapter.SongViewHolder>() {
//
//    inner class SongViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val title: TextView = itemView.findViewById(R.id.songTitle)
//        val artist: TextView = itemView.findViewById(R.id.songArtist)
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
//        val view = LayoutInflater.from(parent.context)
//            .inflate(R.layout.song_card, parent, false)
//        return SongViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
//        val song = songs[position]
//        holder.title.text = song.title
//        holder.artist.text = song.artist
//
//        holder.itemView.setOnClickListener {
//            onSongClick(song)
//        }
//    }
//
//    override fun getItemCount() = songs.size
//}
package com.example.ganimusicapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class SongAdapter(
    private val songs: List<Song>,
    private val onSongClick: (Song) -> Unit
) : RecyclerView.Adapter<SongAdapter.SongViewHolder>() {

    inner class SongViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.songTitle)
        val artist: TextView = itemView.findViewById(R.id.songArtist)
        val image: ImageView = itemView.findViewById(R.id.songImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.song_card, parent, false)
        return SongViewHolder(view)
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        val song = songs[position]
        holder.title.text = song.title
        holder.artist.text = song.artist

        // Load image with Glide
        Glide.with(holder.itemView.context)
            .load(song.image) // image URL or resource
            .placeholder(R.drawable.ic_music_placeholder)
            .error(R.drawable.ic_music_placeholder)
            .into(holder.image)

        holder.itemView.setOnClickListener {
            onSongClick(song)
        }
    }

    override fun getItemCount(): Int = songs.size
}
