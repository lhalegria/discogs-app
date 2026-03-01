package com.example.discogsapp.domain.usecase.release

import com.example.discogsapp.domain.model.ArtistReleaseModel
import com.example.discogsapp.domain.model.ArtistReleaseQueryModel
import com.example.discogsapp.domain.model.ArtistReleaseSearchModel
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

class SearchArtistReleaseUseCaseTest {
    @Test
    fun `SearchArtistReleaseUseCase delegates to repository`() =
        runTest {
            val expected =
                ArtistReleaseSearchModel(
                    pagination = PaginationModel(page = 1, perPage = 30, pages = 1, items = 1),
                    releases =
                        listOf(
                            ArtistReleaseModel(
                                id = 29401633,
                                title = "Paramore - Paramore",
                                year = "2024",
                                type = "release",
                                thumbnailUrl = "",
                                label = listOf("Fueled By Ramen", "Atlantic Recording Corporation"),
                                genre = listOf("Rock", "Pop"),
                            ),
                        ),
                )
            val repository = mockk<ArtistRepository>()
            val useCase = SearchArtistReleaseUseCase(repository)
            val query = ArtistReleaseQueryModel(page = 1, perPage = 30, artist = "Paramore")

            every { repository.searchArtistReleases(query) } returns flowOf(expected)

            val result = useCase(query).first()

            verify(exactly = 1) { repository.searchArtistReleases(query) }
            assertEquals(expected, result)
        }
}
