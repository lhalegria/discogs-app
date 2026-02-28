package com.example.discogsapp.domain.usecase.artist

import com.example.discogsapp.domain.model.ArtistDetailsModel
import com.example.discogsapp.domain.model.ArtistReleaseModel
import com.example.discogsapp.domain.model.ArtistReleasesQueryModel
import com.example.discogsapp.domain.model.ArtistReleasesResultModel
import com.example.discogsapp.domain.model.ArtistSearchQueryModel
import com.example.discogsapp.domain.model.ArtistSearchResultModel
import com.example.discogsapp.domain.model.ArtistSummaryModel
import com.example.discogsapp.domain.model.PaginationModel
import com.example.discogsapp.domain.repository.ArtistRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class ArtistUseCasesTest {
    @Test
    fun `SearchArtistsUseCase delegates to repository`() =
        runTest {
            val expected =
                ArtistSearchResultModel(
                    pagination = PaginationModel(page = 1, perPage = 30, pages = 1, items = 1),
                    artists = listOf(ArtistSummaryModel(id = 1, title = "Justice", thumbnailUrl = "", type = "artist")),
                )
            val repository = mockk<ArtistRepository>()
            val useCase = SearchArtistsUseCase(repository)
            val query = ArtistSearchQueryModel(query = "justice", page = 1, perPage = 30)

            every { repository.searchArtists(query) } returns flowOf(expected)

            val result = useCase(query).first()

            verify(exactly = 1) { repository.searchArtists(query) }
            assertEquals(expected, result)
        }

    @Test
    fun `GetArtistDetailsUseCase delegates to repository`() =
        runTest {
            val expected = ArtistDetailsModel(id = 7, name = "Bonobo")
            val repository = mockk<ArtistRepository>()
            val useCase = GetArtistDetailsUseCase(repository)

            every { repository.getArtistDetails(7) } returns flowOf(expected)

            val result = useCase(7).first()

            verify(exactly = 1) { repository.getArtistDetails(7) }
            assertEquals(expected, result)
        }

    @Test
    fun `GetArtistReleasesUseCase delegates to repository`() =
        runTest {
            val expected =
                ArtistReleasesResultModel(
                    pagination = PaginationModel(page = 1, perPage = 20, pages = 2, items = 40),
                    releases =
                        listOf(
                            ArtistReleaseModel(
                                id = 100,
                                title = "Cross",
                                year = "2007",
                                role = "Main",
                                type = "release",
                                thumbnailUrl = "",
                                label = "Roadrunner",
                            ),
                        ),
                )
            val repository = mockk<ArtistRepository>()
            val useCase = GetArtistReleasesUseCase(repository)
            val query =
                ArtistReleasesQueryModel(
                    artistId = 1000,
                    page = 1,
                    perPage = 20,
                    sort = "year",
                    sortOrder = "desc",
                )

            every { repository.getArtistReleases(query) } returns flowOf(expected)

            val result = useCase(query).first()

            verify(exactly = 1) { repository.getArtistReleases(query) }
            assertEquals(expected, result)
        }
}
