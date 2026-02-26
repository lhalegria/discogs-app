package com.example.discogsapp.domain.model

data class ArtistReleasesResultModel(
    val pagination: PaginationModel,
    val releases: List<ArtistReleaseModel>,
)
