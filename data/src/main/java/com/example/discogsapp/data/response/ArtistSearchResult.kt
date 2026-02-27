package com.example.discogsapp.data.response

import com.squareup.moshi.Json

data class ArtistSearchResult(
    val id: Int,
    val title: String,
    val genre: List<String>? = null,
    @Json(name = "thumb") val thumbnailUrl: String? = null,
    val type: String,
)
