package com.example.discogsapp.data.response

import com.squareup.moshi.Json

data class ArtistRelease(
    val id: Int,
    val title: String,
    val year: Int? = null,
    val role: String? = null,
    val type: String? = null,
    @Json(name = "thumb") val thumbnailUrl: String? = null,
    val label: String? = null,
)
