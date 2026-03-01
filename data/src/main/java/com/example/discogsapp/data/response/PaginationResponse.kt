package com.example.discogsapp.data.response

import com.squareup.moshi.Json

data class PaginationResponse(
    val page: Int,
    @Json(name = "per_page") val perPage: Int,
    val pages: Int,
    val items: Int,
)
