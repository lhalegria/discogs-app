package com.example.discogsapp.domain.model

data class ArtistSearchQueryModel(
    val query: String,
    val page: Int,
    val perPage: Int,
)
