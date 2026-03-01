package com.example.discogsapp.data.response

import com.squareup.moshi.Json

data class ArtistImageResponse(
    val uri: String,
    @Json(name = "uri150") val smallUri: String? = null,
    val width: Int? = null,
    val height: Int? = null,
)
