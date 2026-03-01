package com.example.discogsapp.domain.usecase.artist

import com.example.discogsapp.domain.model.ArtistQueryModel
import com.example.discogsapp.domain.model.ArtistSearchModel
import com.example.discogsapp.domain.repository.ArtistRepository
import com.example.discogsapp.domain.usecase.UseCase
import kotlinx.coroutines.flow.Flow

class SearchArtistUseCase(
    private val artistRepository: ArtistRepository,
) : UseCase<ArtistQueryModel, ArtistSearchModel> {
    override fun invoke(param: ArtistQueryModel): Flow<ArtistSearchModel> =
        artistRepository.searchArtists(param)
}
