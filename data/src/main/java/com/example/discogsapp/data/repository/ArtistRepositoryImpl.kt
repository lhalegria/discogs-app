package com.example.discogsapp.data.repository

import com.example.discogsapp.data.extension.parseHttpError
import com.example.discogsapp.data.mapper.toDomain
import com.example.discogsapp.data.service.DiscogsService
import com.example.discogsapp.domain.model.ArtistDetailModel
import com.example.discogsapp.domain.model.ArtistQueryModel
import com.example.discogsapp.domain.model.ArtistReleaseQueryModel
import com.example.discogsapp.domain.model.ArtistReleaseSearchModel
import com.example.discogsapp.domain.model.ArtistSearchModel
import com.example.discogsapp.domain.repository.ArtistRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ArtistRepositoryImpl @Inject constructor(
    private val discogsService: DiscogsService,
) : ArtistRepository {
    override fun searchArtists(query: ArtistQueryModel): Flow<ArtistSearchModel> =
        flow {
            emit(
                discogsService
                    .searchArtists(
                        query = query.query,
                        page = query.page,
                        perPage = query.perPage,
                    ).toDomain(),
            )
        }.parseHttpError()

    override fun getArtistDetails(artistId: Int): Flow<ArtistDetailModel> =
        flow {
            emit(discogsService.getArtistDetails(artistId).toDomain())
        }.parseHttpError()

    override fun searchArtistReleases(query: ArtistReleaseQueryModel): Flow<ArtistReleaseSearchModel> =
        flow {
            emit(
                discogsService
                    .searchArtistReleases(
                        type = "release",
                        artist = query.artist,
                        year = query.year,
                        genre = query.genre,
                        label = query.label,
                        page = query.page,
                        perPage = query.perPage,
                    ).toDomain(),
            )
        }.parseHttpError()
}
