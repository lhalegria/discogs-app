package com.example.discogsapp.di.data

import com.example.discogsapp.data.network.retrofitAPI
import com.example.discogsapp.data.repository.ArtistRepositoryImpl
import com.example.discogsapp.data.service.DiscogsService
import com.example.discogsapp.domain.repository.ArtistRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {
    @Binds
    @Singleton
    abstract fun bindArtistRepository(artistRepositoryImpl: ArtistRepositoryImpl): ArtistRepository

    companion object {
        @Provides
        @Singleton
        fun provideDiscogsService(): DiscogsService = retrofitAPI().create(DiscogsService::class.java)
    }
}
