package com.example.discogsapp.main.viewmodel

import app.cash.turbine.test
import com.example.discogsapp.domain.usecase.artist.SearchArtistUseCase
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class MainViewModelTest {
    @Test
    fun `onQueryChanged trims query and marks hasSearched true for non blank input`() =
        runTest {
            val viewModel = MainViewModel(searchArtistUseCase = mockk<SearchArtistUseCase>(relaxed = true))

            viewModel.state.test {
                assertEquals(MainState(), awaitItem())

                viewModel.onQueryChanged("  Daft Punk  ")

                assertEquals(
                    MainState(query = "  Daft Punk  ", hasSearched = true),
                    awaitItem(),
                )
            }
        }

    @Test
    fun `onQueryChanged marks hasSearched false for blank input`() =
        runTest {
            val viewModel = MainViewModel(searchArtistUseCase = mockk<SearchArtistUseCase>(relaxed = true))

            viewModel.state.test {
                assertEquals(MainState(), awaitItem())

                viewModel.onQueryChanged("   ")

                assertEquals(MainState(query = "   ", hasSearched = false), awaitItem())
            }
        }
}
