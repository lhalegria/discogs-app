package com.example.discogsapp.domain.model

data class ArtistDetailsModel(
    val id: Int,
    val name: String,
    val profile: String = "",
    val realName: String = "",
    val urls: List<String> = emptyList(),
    val images: List<ArtistImageModel> = emptyList(),
    val members: List<ArtistMemberModel> = emptyList(),
)
