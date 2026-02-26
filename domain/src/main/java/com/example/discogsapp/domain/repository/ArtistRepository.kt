package com.example.discogsapp.domain.repository

import com.example.discogsapp.domain.model.ArtistDetailsModel
import com.example.discogsapp.domain.model.ArtistReleasesQueryModel
import com.example.discogsapp.domain.model.ArtistReleasesResultModel
import com.example.discogsapp.domain.model.ArtistSearchQueryModel
import com.example.discogsapp.domain.model.ArtistSearchResultModel

interface ArtistRepository {
    suspend fun searchArtists(query: ArtistSearchQueryModel): ArtistSearchResultModel

    suspend fun getArtistDetails(artistId: Int): ArtistDetailsModel

    suspend fun getArtistReleases(query: ArtistReleasesQueryModel): ArtistReleasesResultModel
}
