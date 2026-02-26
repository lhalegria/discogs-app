package com.example.discogsapp.domain.model

data class ArtistReleasesQueryModel(
    val artistId: Int,
    val page: Int,
    val perPage: Int,
    val sort: String,
    val sortOrder: String,
)
