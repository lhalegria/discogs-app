package com.example.discogsapp.domain.usecase.artist

import com.example.discogsapp.domain.model.ArtistDetailModel
import com.example.discogsapp.domain.repository.ArtistRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class GetArtistDetailUseCaseTest {
    @Test
    fun `GetArtistDetailUseCase delegates to repository`() =
        runTest {
            val expected = ArtistDetailModel(id = 7, name = "Bonobo")
            val repository = mockk<ArtistRepository>()
            val useCase = GetArtistDetailUseCase(repository)

            every { repository.getArtistDetails(7) } returns flowOf(expected)

            val result = useCase(7).first()

            verify(exactly = 1) { repository.getArtistDetails(7) }
            assertEquals(expected, result)
        }
}
