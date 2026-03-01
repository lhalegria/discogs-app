package com.example.discogsapp.data.response

data class ArtistReleaseSearchResponse(
    val pagination: PaginationResponse,
    val results: List<ArtistReleaseSearchDataResponse>,
)
