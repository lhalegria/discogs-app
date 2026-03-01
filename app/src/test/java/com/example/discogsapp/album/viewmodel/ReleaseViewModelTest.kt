package com.example.discogsapp.album.viewmodel

import app.cash.turbine.test
import com.example.discogsapp.domain.usecase.release.SearchArtistReleaseUseCase
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class ReleaseViewModelTest {
    private val searchArtistReleaseUseCase = mockk<SearchArtistReleaseUseCase>(relaxed = true)

    @Test
    fun `onQueryChanged updates query`() =
        runTest {
            val viewModel = createViewModel()

            viewModel.state.test {
                assertEquals(ReleaseState(), awaitItem())

                viewModel.onQueryChanged(" 1997 ")

                assertEquals(
                    ReleaseState(query = " 1997 "),
                    awaitItem(),
                )
            }
        }

    @Test
    fun `setYearFilter enables only year filter and clears query`() =
        runTest {
            val viewModel = createViewModel()

            viewModel.state.test {
                assertEquals(ReleaseState(), awaitItem())

                viewModel.onQueryChanged("1997")
                assertEquals(ReleaseState(query = "1997"), awaitItem())

                viewModel.setYearFilter()
                assertEquals(
                    ReleaseState(query = "", isYearFilter = true, isGenreFilter = false, isLabelFilter = false),
                    awaitItem(),
                )
            }
        }

    @Test
    fun `setGenreFilter enables only genre filter`() =
        runTest {
            val viewModel = createViewModel()

            viewModel.state.test {
                assertEquals(ReleaseState(), awaitItem())

                viewModel.setGenreFilter()

                assertEquals(
                    ReleaseState(query = "", isYearFilter = false, isGenreFilter = true, isLabelFilter = false),
                    awaitItem(),
                )
            }
        }

    @Test
    fun `setLabelFilter enables only label filter`() =
        runTest {
            val viewModel = createViewModel()

            viewModel.state.test {
                assertEquals(ReleaseState(), awaitItem())

                viewModel.setLabelFilter()

                assertEquals(
                    ReleaseState(query = "", isYearFilter = false, isGenreFilter = false, isLabelFilter = true),
                    awaitItem(),
                )
            }
        }

    private fun createViewModel(): ReleaseViewModel =
        ReleaseViewModel(searchArtistReleaseUseCase = searchArtistReleaseUseCase)
}
