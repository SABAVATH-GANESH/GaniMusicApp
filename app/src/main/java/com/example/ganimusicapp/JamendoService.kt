package com.example.ganimusicapp

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface JamendoService {
    @GET("tracks")
    fun getTracks(
        @Query("client_id") clientId: String,
        @Query("format") format: String = "json",
        @Query("limit") limit: Int = 10,
        @Query("tags") tags: String = "pop",  // Changed from "india" to "pop"
        @Query("audioformat") audioFormat: String = "mp31" // Get mp3 audio
    ): Call<JamendoResponse>
}
