package com.example.discogsapp.domain.model

data class ArtistReleaseSearchModel(
    val pagination: PaginationModel,
    val releases: List<ArtistReleaseModel>,
)
