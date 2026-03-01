package com.example.discogsapp.domain.usecase.release

import com.example.discogsapp.domain.model.ArtistReleaseQueryModel
import com.example.discogsapp.domain.model.ArtistReleaseSearchModel
import com.example.discogsapp.domain.repository.ArtistRepository
import com.example.discogsapp.domain.usecase.UseCase
import kotlinx.coroutines.flow.Flow

class SearchArtistReleaseUseCase(
    private val artistRepository: ArtistRepository,
) : UseCase<ArtistReleaseQueryModel, ArtistReleaseSearchModel> {
    override fun invoke(param: ArtistReleaseQueryModel): Flow<ArtistReleaseSearchModel> =
        artistRepository.searchArtistReleases(param)
}
