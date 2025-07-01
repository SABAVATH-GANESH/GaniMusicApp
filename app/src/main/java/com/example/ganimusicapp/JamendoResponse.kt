package com.example.ganimusicapp

data class JamendoResponse(
    val headers: Headers?, // optional: useful for debugging
    val results: List<JamendoTrack>
)

data class Headers(
    val status: String,
    val code: Int,
    val error_message: String?,
    val warnings: String?,
    val results_count: Int
)
