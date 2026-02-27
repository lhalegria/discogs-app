package com.example.discogsapp.domain.model

data class ArtistSummaryModel(
    val id: Int,
    val title: String,
    val genres: List<String>,
    val thumbnailUrl: String,
    val type: String,
)
