package com.example.discogsapp.data.response

import com.squareup.moshi.Json

data class ArtistDetailsResponse(
    val id: Int,
    val name: String,
    val profile: String? = null,
    @Json(name = "realname") val realName: String? = null,
    val urls: List<String>? = null,
    val images: List<ArtistImageResponse>? = null,
)
