package com.example.discogsapp.domain.model

data class ArtistReleaseQueryModel(
    val page: Int,
    val perPage: Int,
    val artist: String,
    val year: String? = null,
    val genre: String? = null,
    val label: String? = null,
)
