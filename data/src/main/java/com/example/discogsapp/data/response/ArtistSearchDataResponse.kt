package com.example.discogsapp.data.response

import com.squareup.moshi.Json

data class ArtistSearchDataResponse(
    val id: Int,
    val title: String,
    @Json(name = "thumb") val thumbnailUrl: String? = null,
    val type: String,
)
