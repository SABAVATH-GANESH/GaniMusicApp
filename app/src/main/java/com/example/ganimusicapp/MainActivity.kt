package com.example.ganimusicapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.widget.SearchView
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: View
    private lateinit var searchView: SearchView
    private val songList = ArrayList<Song>()
    private lateinit var adapter: SongAdapter
    private lateinit var api: JamendoService   // Retrofit instance

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.songRecyclerView)
        progressBar = findViewById(R.id.progressBar)
        searchView = findViewById(R.id.searchView)

        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = SongAdapter(songList) { song ->
            if (song.url.isNullOrBlank()) {
                Toast.makeText(this, "Song URL not available", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(this, PlayerActivity::class.java).apply {
                    putStringArrayListExtra("playlist", arrayListOf(song.url)) // ✅ always pass playlist
                    putExtra("startIndex", 0)
                    putExtra("title", song.title)
                    putExtra("artist", song.artist)
                    putExtra("image", song.image)
                }
                startActivity(intent)

            }
        }
        recyclerView.adapter = adapter

        // Retrofit instance
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.jamendo.com/v3.0/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        api = retrofit.create(JamendoService::class.java)

        // Default search
        fetchSongsFromJamendo("pop")

        // SearchView listener
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    if (it.isNotBlank()) {
                        fetchSongsFromJamendo(it.trim())
                        searchView.clearFocus()
                    }
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean = false
        })
    }

    private fun fetchSongsFromJamendo(tag: String) {
        progressBar.visibility = View.VISIBLE

        api.getTracks(
            clientId = "dde1e7c5",   // Replace with your Jamendo client_id
            limit = 50,
            tags = tag
        ).enqueue(object : Callback<JamendoResponse> {
            override fun onResponse(
                call: Call<JamendoResponse>,
                response: Response<JamendoResponse>
            ) {
                progressBar.visibility = View.GONE

                if (response.isSuccessful) {
                    val results = response.body()?.results.orEmpty()
                    Log.d("JamendoAPI", "Success: ${results.size} tracks found")

                    songList.clear()
                    songList.addAll(results.map {
                        Song(
                            title = it.name ?: "No Title",
                            artist = it.artist_name ?: "Unknown Artist",
                            url = it.audio ?: "",
                            image = it.image ?: ""
                        )
                    })

                    if (songList.isEmpty()) {
                        Toast.makeText(this@MainActivity, "No songs found for '$tag'", Toast.LENGTH_SHORT).show()
                    }

                    adapter.notifyDataSetChanged()

                } else {
                    Log.e("JamendoAPI", "Error Code: ${response.code()}")
                    Toast.makeText(this@MainActivity, "Error: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<JamendoResponse>, t: Throwable) {
                progressBar.visibility = View.GONE
                Log.e("JamendoAPI", "Failure: ${t.message}", t)
                Toast.makeText(this@MainActivity, "Error: ${t.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        ExoPlayerHelper.release()
    }
}
