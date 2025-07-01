package com.example.ganimusicapp

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.songRecyclerView)
        progressBar = findViewById(R.id.progressBar)
        searchView = findViewById(R.id.searchView)

        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = SongAdapter(songList) { song ->
            ExoPlayerHelper.play(this, song.url)
        }
        recyclerView.adapter = adapter

        // Default search: "pop"
        fetchSongsFromJamendo("pop")

        // Search when user submits a tag
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

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.jamendo.com/v3.0/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(JamendoService::class.java)

        api.getTracks(
            clientId = "dde1e7c5",
            limit = 100,
            tags = tag
        ).enqueue(object : Callback<JamendoResponse> {
            override fun onResponse(
                call: Call<JamendoResponse>,
                response: Response<JamendoResponse>
            ) {
                progressBar.visibility = View.GONE

                if (response.isSuccessful) {
                    val results = response.body()?.results
                    Log.d("JamendoAPI", "Success: $results")

                    if (!results.isNullOrEmpty()) {
                        songList.clear()
                        songList.addAll(results.map {
                            Song(
                                title = it.name ?: "No Title",
                                artist = it.artist_name ?: "Unknown Artist",
                                url = it.audio ?: "",
                                image = it.image ?: ""
                            )
                        })
                        adapter.notifyDataSetChanged()
                    } else {
                        Toast.makeText(this@MainActivity, "No songs found for '$tag'", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.e("JamendoAPI", "Error Code: ${response.code()}")
                    Toast.makeText(this@MainActivity, "Error: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<JamendoResponse>, t: Throwable) {
                progressBar.visibility = View.GONE
                Log.e("JamendoAPI", "Failure: ${t.message}")
                Toast.makeText(this@MainActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        ExoPlayerHelper.release()
    }
}
