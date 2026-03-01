package com.example.discogsapp.data.repository

import com.example.discogsapp.data.response.ArtistDetailsResponse
import com.example.discogsapp.data.response.ArtistImageResponse
import com.example.discogsapp.data.response.ArtistReleaseSearchDataResponse
import com.example.discogsapp.data.response.ArtistReleaseSearchResponse
import com.example.discogsapp.data.response.ArtistSearchDataResponse
import com.example.discogsapp.data.response.ArtistSearchResponse
import com.example.discogsapp.data.response.PaginationResponse
import com.example.discogsapp.data.service.DiscogsService
import com.example.discogsapp.domain.model.ArtistQueryModel
import com.example.discogsapp.domain.model.ArtistReleaseQueryModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class ArtistRepositoryImplTest {
    @Test
    fun `searchArtists maps service response and forwards query params`() =
        runTest {
            val service = mockk<DiscogsService>()
            coEvery {
                service.searchArtists(
                    query = "daft",
                    page = 2,
                    perPage = 15,
                )
            } returns
                ArtistSearchResponse(
                    pagination = PaginationResponse(page = 2, perPage = 15, pages = 4, items = 60),
                    results =
                        listOf(
                            ArtistSearchDataResponse(
                                id = 10,
                                title = "Daft Punk",
                                thumbnailUrl = "https://img",
                                type = "artist",
                            ),
                        ),
                )
            val repository = ArtistRepositoryImpl(service)

            val result =
                repository
                    .searchArtists(
                        ArtistQueryModel(query = "daft", page = 2, perPage = 15),
                    ).first()

            coVerify(exactly = 1) {
                service.searchArtists(
                    query = "daft",
                    page = 2,
                    perPage = 15,
                )
            }
            assertEquals(2, result.pagination.page)
            assertEquals(1, result.artists.size)
            assertEquals("Daft Punk", result.artists.first().title)
        }

    @Test
    fun `getArtistDetails maps service response and forwards artist id`() =
        runTest {
            val service = mockk<DiscogsService>()
            coEvery {
                service.getArtistDetails(20)
            } returns
                ArtistDetailsResponse(
                    id = 20,
                    name = "Apex Twin",
                    profile = null,
                    realName = null,
                    urls = null,
                    images =
                        listOf(
                            ArtistImageResponse(
                                uri = "https://full",
                                smallUri = null,
                                width = null,
                                height = null,
                            ),
                        ),
                )
            val repository = ArtistRepositoryImpl(service)

            val result = repository.getArtistDetails(20).first()

            coVerify(exactly = 1) { service.getArtistDetails(20) }
            assertEquals("Apex Twin", result.name)
            assertEquals("", result.profile)
            assertEquals("", result.images.first().smallUri)
        }

    @Test
    fun `searchArtistReleases maps service response and forwards query params`() =
        runTest {
            val service = mockk<DiscogsService>()
            coEvery {
                service.searchArtistReleases(
                    artist = "Daft Punk",
                    page = 2,
                    perPage = 15,
                )
            } returns
                ArtistReleaseSearchResponse(
                    pagination = PaginationResponse(page = 2, perPage = 15, pages = 4, items = 60),
                    results =
                        listOf(
                            ArtistReleaseSearchDataResponse(
                                id = 99,
                                title = "Homework",
                                year = "2025",
                                role = null,
                                type = "release",
                                genre = listOf("Electronic"),
                                thumbnailUrl = null,
                            ),
                        ),
                )
            val repository = ArtistRepositoryImpl(service)

            val result =
                repository
                    .searchArtistReleases(
                        ArtistReleaseQueryModel(artist = "Daft Punk", page = 2, perPage = 15),
                    ).first()

            coVerify(exactly = 1) {
                service.searchArtistReleases(
                    artist = "Daft Punk",
                    page = 2,
                    perPage = 15,
                )
            }
            assertEquals(2, result.pagination.page)
            assertEquals(1, result.releases.size)
            assertEquals("Homework", result.releases.first().title)
        }
}
