package com.example.discogsapp.di.domain

import com.example.discogsapp.domain.repository.ArtistRepository
import com.example.discogsapp.domain.usecase.artist.GetArtistDetailsUseCase
import com.example.discogsapp.domain.usecase.artist.GetArtistReleasesUseCase
import com.example.discogsapp.domain.usecase.artist.SearchArtistsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DomainModule {
    @Provides
    fun provideSearchArtistsUseCase(artistRepository: ArtistRepository) = SearchArtistsUseCase(artistRepository)

    @Provides
    fun provideGetArtistDetailsUseCase(artistRepository: ArtistRepository) = GetArtistDetailsUseCase(artistRepository)

    @Provides
    fun provideGetArtistReleasesUseCase(artistRepository: ArtistRepository) = GetArtistReleasesUseCase(artistRepository)
}
