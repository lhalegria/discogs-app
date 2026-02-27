package com.example.discogsapp.domain.usecase.artist

import com.example.discogsapp.domain.model.ArtistSearchQueryModel
import com.example.discogsapp.domain.model.ArtistSearchResultModel
import com.example.discogsapp.domain.repository.ArtistRepository
import com.example.discogsapp.domain.usecase.UseCase
import kotlinx.coroutines.flow.Flow

class SearchArtistsUseCase(
    private val artistRepository: ArtistRepository,
) : UseCase<ArtistSearchQueryModel, ArtistSearchResultModel> {

    override fun invoke(param: ArtistSearchQueryModel): Flow<ArtistSearchResultModel> =
        artistRepository.searchArtists(param)
}
