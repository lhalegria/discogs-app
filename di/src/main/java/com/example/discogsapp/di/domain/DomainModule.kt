package com.example.discogsapp.di.domain

import com.example.discogsapp.domain.repository.ArtistRepository
import com.example.discogsapp.domain.usecase.artist.GetArtistDetailUseCase
import com.example.discogsapp.domain.usecase.artist.SearchArtistUseCase
import com.example.discogsapp.domain.usecase.release.SearchArtistReleaseUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DomainModule {
    @Provides
    fun provideSearchArtistsUseCase(artistRepository: ArtistRepository) = SearchArtistUseCase(artistRepository)

    @Provides
    fun provideGetArtistDetailsUseCase(artistRepository: ArtistRepository) = GetArtistDetailUseCase(artistRepository)

    @Provides
    fun provideSearchArtistReleasesUseCase(artistRepository: ArtistRepository) =
        SearchArtistReleaseUseCase(artistRepository)
}
