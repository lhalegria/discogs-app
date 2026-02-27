package com.example.discogsapp.domain.repository

import com.example.discogsapp.domain.model.ArtistDetailsModel
import com.example.discogsapp.domain.model.ArtistReleasesQueryModel
import com.example.discogsapp.domain.model.ArtistReleasesResultModel
import com.example.discogsapp.domain.model.ArtistSearchQueryModel
import com.example.discogsapp.domain.model.ArtistSearchResultModel
import kotlinx.coroutines.flow.Flow

interface ArtistRepository {
    fun searchArtists(query: ArtistSearchQueryModel): Flow<ArtistSearchResultModel>

    fun getArtistDetails(artistId: Int): Flow<ArtistDetailsModel>

    fun getArtistReleases(query: ArtistReleasesQueryModel): Flow<ArtistReleasesResultModel>
}
