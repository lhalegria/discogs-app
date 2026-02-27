package com.example.discogsapp.data.repository

import com.example.discogsapp.data.response.ArtistDetailsResponse
import com.example.discogsapp.data.response.ArtistImage
import com.example.discogsapp.data.response.ArtistRelease
import com.example.discogsapp.data.response.ArtistReleasesResponse
import com.example.discogsapp.data.response.ArtistSearchResponse
import com.example.discogsapp.data.response.ArtistSearchResult
import com.example.discogsapp.data.response.Pagination
import com.example.discogsapp.data.service.DiscogsService
import com.example.discogsapp.domain.model.ArtistReleasesQueryModel
import com.example.discogsapp.domain.model.ArtistSearchQueryModel
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
                    pagination = Pagination(page = 2, perPage = 15, pages = 4, items = 60),
                    results =
                        listOf(
                            ArtistSearchResult(
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
                        ArtistSearchQueryModel(query = "daft", page = 2, perPage = 15),
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
                    images = listOf(ArtistImage(uri = "https://full", smallUri = null, width = null, height = null)),
                )
            val repository = ArtistRepositoryImpl(service)

            val result = repository.getArtistDetails(20).first()

            coVerify(exactly = 1) { service.getArtistDetails(20) }
            assertEquals("Apex Twin", result.name)
            assertEquals("", result.profile)
            assertEquals("", result.images.first().smallUri)
        }

    @Test
    fun `getArtistReleases maps service response and forwards query params`() =
        runTest {
            val service = mockk<DiscogsService>()
            coEvery {
                service.getArtistReleases(
                    artistId = 30,
                    page = 3,
                    perPage = 25,
                    sort = "year",
                    sortOrder = "asc",
                )
            } returns
                ArtistReleasesResponse(
                    pagination = Pagination(page = 3, perPage = 25, pages = 7, items = 150),
                    releases =
                        listOf(
                            ArtistRelease(
                                id = 99,
                                title = "Homework",
                                year = null,
                                role = null,
                                type = null,
                                thumbnailUrl = null,
                            ),
                        ),
                )
            val repository = ArtistRepositoryImpl(service)

            val result =
                repository
                    .getArtistReleases(
                        ArtistReleasesQueryModel(
                            artistId = 30,
                            page = 3,
                            perPage = 25,
                            sort = "year",
                            sortOrder = "asc",
                        ),
                    ).first()

            coVerify(exactly = 1) {
                service.getArtistReleases(
                    artistId = 30,
                    page = 3,
                    perPage = 25,
                    sort = "year",
                    sortOrder = "asc",
                )
            }
            assertEquals("", result.releases.first().role)
            assertEquals(0, result.releases.first().year)
        }
}
