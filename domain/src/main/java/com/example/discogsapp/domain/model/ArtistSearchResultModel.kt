package com.example.discogsapp.domain.model

data class ArtistSearchResultModel(
    val pagination: PaginationModel,
    val artists: List<ArtistSummaryModel>,
)
