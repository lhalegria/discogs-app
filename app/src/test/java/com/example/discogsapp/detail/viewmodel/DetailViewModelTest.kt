package com.example.discogsapp.detail.viewmodel

import app.cash.turbine.test
import com.example.discogsapp.domain.model.ArtistDetailModel
import com.example.discogsapp.domain.usecase.artist.GetArtistDetailUseCase
import com.example.discogsapp.release.navigation.ReleaseRoute
import com.example.discogsapp.testing.MainDispatcherRule
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class DetailViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val useCase = mockk<GetArtistDetailUseCase>(relaxed = true)

    @Before
    fun setUp() {
        clearMocks(useCase)
    }

    @Test
    fun `fetchArtistDetails updates loading and success state`() =
        runTest {
            every { useCase(42) } returns flowOf(ArtistDetailModel(id = 42, name = "Daft Punk"))
            val viewModel = createViewModel()

            viewModel.state.test {
                assertEquals(DetailState(), awaitItem())

                viewModel.fetchArtistDetails(42)

                assertEquals(
                    DetailState(artistId = 42, isLoading = true, hasError = false),
                    awaitItem(),
                )
                assertEquals(
                    DetailState(
                        artistId = 42,
                        artistInfo = ArtistDetailModel(id = 42, name = "Daft Punk"),
                        isLoading = true,
                        hasError = false,
                    ),
                    awaitItem(),
                )
                assertEquals(
                    DetailState(
                        artistId = 42,
                        artistInfo = ArtistDetailModel(id = 42, name = "Daft Punk"),
                        isLoading = false,
                        hasError = false,
                    ),
                    awaitItem(),
                )
            }
        }

    @Test
    fun `fetchArtistDetails updates error state when use case fails`() =
        runTest {
            every { useCase(7) } returns flow { error("network") }
            val viewModel = createViewModel()

            viewModel.state.test {
                assertEquals(DetailState(), awaitItem())

                viewModel.fetchArtistDetails(7)

                assertEquals(
                    DetailState(artistId = 7, isLoading = true, hasError = false),
                    awaitItem(),
                )
                assertEquals(
                    DetailState(artistId = 7, isLoading = false, hasError = true),
                    awaitItem(),
                )
            }
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `navigateToRelease emits navigation effect`() =
        runTest {
            val viewModel = createViewModel()

            viewModel.effect.test {
                viewModel.navigateToRelease("Daft Punk")
                advanceUntilIdle()

                assertEquals(
                    DetailEffect.NavigateToRelease(ReleaseRoute("Daft Punk")),
                    awaitItem(),
                )
            }
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `openUrl emits open url effect`() =
        runTest {
            val viewModel = createViewModel()

            viewModel.effect.test {
                viewModel.openUrl("https://discogs.com")
                advanceUntilIdle()

                assertEquals(DetailEffect.OpenUrl("https://discogs.com"), awaitItem())
            }
        }

    private fun createViewModel(): DetailViewModel = DetailViewModel(useCase, mainDispatcherRule.dispatcher)
}
