package com.example.discogsapp.data.repository

import com.example.discogsapp.data.extension.parseHttpError
import com.example.discogsapp.data.mapper.toDomain
import com.example.discogsapp.data.service.DiscogsService
import com.example.discogsapp.domain.model.ArtistDetailsModel
import com.example.discogsapp.domain.model.ArtistReleasesQueryModel
import com.example.discogsapp.domain.model.ArtistReleasesResultModel
import com.example.discogsapp.domain.model.ArtistSearchQueryModel
import com.example.discogsapp.domain.model.ArtistSearchResultModel
import com.example.discogsapp.domain.repository.ArtistRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ArtistRepositoryImpl(
    private val discogsService: DiscogsService,
) : ArtistRepository {

    override fun searchArtists(query: ArtistSearchQueryModel): Flow<ArtistSearchResultModel> = flow {
        emit(
            discogsService.searchArtists(
                query = query.query,
                page = query.page,
                perPage = query.perPage,
            ).toDomain(),
        )
    }.parseHttpError()

    override fun getArtistDetails(artistId: Int): Flow<ArtistDetailsModel> = flow {
        emit(discogsService.getArtistDetails(artistId).toDomain())
    }.parseHttpError()

    override fun getArtistReleases(query: ArtistReleasesQueryModel): Flow<ArtistReleasesResultModel> = flow {
        emit(
            discogsService.getArtistReleases(
                artistId = query.artistId,
                page = query.page,
                perPage = query.perPage,
                sort = query.sort,
                sortOrder = query.sortOrder,
            ).toDomain(),
        )
    }.parseHttpError()
}
