package com.example.discogsapp.data.response

data class ArtistReleasesResponse(
    val pagination: Pagination,
    val releases: List<ArtistRelease>,
)
