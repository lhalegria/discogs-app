package com.example.discogsapp.domain.usecase.artist

import com.example.discogsapp.domain.model.ArtistDetailModel
import com.example.discogsapp.domain.repository.ArtistRepository
import com.example.discogsapp.domain.usecase.UseCase
import kotlinx.coroutines.flow.Flow

class GetArtistDetailUseCase(
    private val artistRepository: ArtistRepository,
) : UseCase<Int, ArtistDetailModel> {
    override fun invoke(param: Int): Flow<ArtistDetailModel> = artistRepository.getArtistDetails(param)
}
