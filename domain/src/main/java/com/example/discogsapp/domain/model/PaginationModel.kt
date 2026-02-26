package com.example.discogsapp.domain.model

data class PaginationModel(
    val page: Int,
    val perPage: Int,
    val pages: Int,
    val items: Int,
)
