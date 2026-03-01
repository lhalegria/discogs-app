package com.example.discogsapp.data.service

import com.example.discogsapp.data.response.ArtistDetailsResponse
import com.example.discogsapp.data.response.ArtistReleaseSearchResponse
import com.example.discogsapp.data.response.ArtistSearchResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface DiscogsService {
    @GET("database/search")
    suspend fun searchArtists(
        @Query("type") type: String = "artist",
        @Query("q") query: String,
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 30,
    ): ArtistSearchResponse

    @GET("artists/{artistId}")
    suspend fun getArtistDetails(
        @Path("artistId") artistId: Int,
    ): ArtistDetailsResponse

    @GET("database/search")
    suspend fun searchArtistReleases(
        @Query("type") type: String = "release",
        @Query("artist") artist: String,
        @Query("year") year: String? = null,
        @Query("genre") genre: String? = null,
        @Query("label") label: String? = null,
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 30,
        @Query("sort") sort: String = "year",
    ): ArtistReleaseSearchResponse
}
