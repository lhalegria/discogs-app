package com.example.discogsapp.data.response

data class ArtistSearchResponse(
    val pagination: Pagination,
    val results: List<ArtistSearchResult>,
)
