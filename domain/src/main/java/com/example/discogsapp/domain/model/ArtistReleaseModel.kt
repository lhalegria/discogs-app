package com.example.discogsapp.domain.model

data class ArtistReleaseModel(
    val id: Int,
    val title: String,
    val year: Int,
    val role: String,
    val type: String,
    val thumbnailUrl: String,
)
