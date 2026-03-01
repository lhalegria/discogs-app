package com.example.discogsapp.domain.model

data class ArtistQueryModel(
    val query: String,
    val page: Int,
    val perPage: Int,
)
