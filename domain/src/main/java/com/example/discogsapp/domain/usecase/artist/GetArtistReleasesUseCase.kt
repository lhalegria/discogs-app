package com.example.discogsapp.domain.usecase.artist

import com.example.discogsapp.domain.model.ArtistReleasesQueryModel
import com.example.discogsapp.domain.model.ArtistReleasesResultModel
import com.example.discogsapp.domain.repository.ArtistRepository
import com.example.discogsapp.domain.usecase.UseCase
import kotlinx.coroutines.flow.Flow

class GetArtistReleasesUseCase(
    private val artistRepository: ArtistRepository,
) : UseCase<ArtistReleasesQueryModel, ArtistReleasesResultModel> {
    override fun invoke(param: ArtistReleasesQueryModel): Flow<ArtistReleasesResultModel> =
        artistRepository.getArtistReleases(param)
}
