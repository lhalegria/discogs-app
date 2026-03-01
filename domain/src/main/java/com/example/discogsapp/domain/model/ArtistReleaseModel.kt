package com.example.discogsapp.domain.model

data class ArtistReleaseModel(
    val id: Int,
    val title: String,
    val year: String,
    val type: String,
    val thumbnailUrl: String,
    val label: List<String>,
    val genre: List<String>,
)
