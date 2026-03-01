package com.example.discogsapp.domain.repository

import com.example.discogsapp.domain.model.ArtistDetailModel
import com.example.discogsapp.domain.model.ArtistQueryModel
import com.example.discogsapp.domain.model.ArtistReleaseQueryModel
import com.example.discogsapp.domain.model.ArtistReleaseSearchModel
import com.example.discogsapp.domain.model.ArtistSearchModel
import kotlinx.coroutines.flow.Flow

interface ArtistRepository {
    fun searchArtists(query: ArtistQueryModel): Flow<ArtistSearchModel>

    fun getArtistDetails(artistId: Int): Flow<ArtistDetailModel>

    fun searchArtistReleases(query: ArtistReleaseQueryModel): Flow<ArtistReleaseSearchModel>
}
