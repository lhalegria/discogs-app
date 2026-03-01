package com.example.discogsapp.data.response

data class ArtistSearchResponse(
    val pagination: PaginationResponse,
    val results: List<ArtistSearchDataResponse>,
)
