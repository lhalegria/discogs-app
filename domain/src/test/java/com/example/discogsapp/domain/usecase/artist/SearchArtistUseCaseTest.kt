package com.example.discogsapp.domain.usecase.artist

import com.example.discogsapp.domain.model.ArtistQueryModel
import com.example.discogsapp.domain.model.ArtistSearchModel
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

class SearchArtistUseCaseTest {
    @Test
    fun `SearchArtistUseCase delegates to repository`() =
        runTest {
            val expected =
                ArtistSearchModel(
                    pagination = PaginationModel(page = 1, perPage = 30, pages = 1, items = 1),
                    artists = listOf(ArtistSummaryModel(id = 1, title = "Justice", thumbnailUrl = "", type = "artist")),
                )
            val repository = mockk<ArtistRepository>()
            val useCase = SearchArtistUseCase(repository)
            val query = ArtistQueryModel(query = "justice", page = 1, perPage = 30)

            every { repository.searchArtists(query) } returns flowOf(expected)

            val result = useCase(query).first()

            verify(exactly = 1) { repository.searchArtists(query) }
            assertEquals(expected, result)
        }
}
