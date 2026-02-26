package com.example.discogsapp.data.repository

import com.example.discogsapp.data.mapper.toDomain
import com.example.discogsapp.data.service.DiscogsService
import com.example.discogsapp.domain.model.ArtistDetailsModel
import com.example.discogsapp.domain.model.ArtistReleasesQueryModel
import com.example.discogsapp.domain.model.ArtistReleasesResultModel
import com.example.discogsapp.domain.model.ArtistSearchQueryModel
import com.example.discogsapp.domain.model.ArtistSearchResultModel
import com.example.discogsapp.domain.repository.ArtistRepository

class ArtistRepositoryImpl(
    private val discogsService: DiscogsService,
) : ArtistRepository {

    override suspend fun searchArtists(query: ArtistSearchQueryModel): ArtistSearchResultModel =
        discogsService.searchArtists(
            query = query.query,
            page = query.page,
            perPage = query.perPage,
        ).toDomain()

    override suspend fun getArtistDetails(artistId: Int): ArtistDetailsModel =
        discogsService.getArtistDetails(artistId).toDomain()

    override suspend fun getArtistReleases(query: ArtistReleasesQueryModel): ArtistReleasesResultModel =
        discogsService.getArtistReleases(
            artistId = query.artistId,
            page = query.page,
            perPage = query.perPage,
            sort = query.sort,
            sortOrder = query.sortOrder,
        ).toDomain()
}
