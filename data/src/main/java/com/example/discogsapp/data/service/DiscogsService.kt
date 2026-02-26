package com.example.discogsapp.data.service

import com.example.discogsapp.data.response.ArtistDetailsResponse
import com.example.discogsapp.data.response.ArtistReleasesResponse
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

    @GET("artists/{artistId}/releases")
    suspend fun getArtistReleases(
        @Path("artistId") artistId: Int,
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 30,
        @Query("sort") sort: String? = null,
        @Query("sort_order") sortOrder: String? = null,
    ): ArtistReleasesResponse
}
