package com.example.discogsapp.domain.usecase.artist

import com.example.discogsapp.domain.model.ArtistDetailsModel
import com.example.discogsapp.domain.repository.ArtistRepository
import com.example.discogsapp.domain.usecase.UseCase
import kotlinx.coroutines.flow.Flow

class GetArtistDetailsUseCase(
    private val artistRepository: ArtistRepository,
) : UseCase<Int, ArtistDetailsModel> {
    override fun invoke(param: Int): Flow<ArtistDetailsModel> = artistRepository.getArtistDetails(param)
}
