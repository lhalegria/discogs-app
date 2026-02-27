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
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    @Singleton
    abstract fun bindArtistRepository(artistRepositoryImpl: ArtistRepositoryImpl): ArtistRepository

    companion object {
        private const val DEFAULT_AUTHORIZATION = ""
        private const val DEFAULT_USER_AGENT = "DiscogsApp/1.0 +https://github.com/lhalegria/discogs-app"

        @Provides
        @Named("discogsAuthorization")
        fun provideDiscogsAuthorization(): String = DEFAULT_AUTHORIZATION

        @Provides
        @Named("discogsUserAgent")
        fun provideDiscogsUserAgent(): String = DEFAULT_USER_AGENT

        @Provides
        fun provideIsDebug(): Boolean = false

        @Provides
        @Singleton
        fun provideDiscogsService(
            @Named("discogsAuthorization") authorization: String,
            @Named("discogsUserAgent") userAgent: String,
            isDebug: Boolean,
        ): DiscogsService = retrofitAPI(
            authorization = authorization,
            userAgent = userAgent,
            isDebug = isDebug,
        ).create(DiscogsService::class.java)
    }
}
