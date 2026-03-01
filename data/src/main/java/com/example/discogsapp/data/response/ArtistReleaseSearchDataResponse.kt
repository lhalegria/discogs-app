package com.example.discogsapp.data.response

import com.squareup.moshi.Json

data class ArtistReleaseSearchDataResponse(
    val id: Int,
    val title: String,
    val year: String? = null,
    val role: String? = null,
    val type: String? = null,
    val label: List<String>? = null,
    val genre: List<String>? = null,
    @Json(name = "thumb") val thumbnailUrl: String? = null,
)
