package com.example.discogsapp.domain.model

data class ArtistSearchModel(
    val pagination: PaginationModel,
    val artists: List<ArtistSummaryModel>,
)
