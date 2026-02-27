package com.example.discogsapp.main.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.discogsapp.domain.model.ArtistSearchQueryModel
import com.example.discogsapp.domain.usecase.artist.SearchArtistsUseCase
import com.example.discogsapp.viewmodel.flow.StateViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

@HiltViewModel
class MainViewModel @Inject constructor(
    private val searchArtistsUseCase: SearchArtistsUseCase,
) : StateViewModel<MainState>(MainState()) {

    private var searchJob: Job? = null

    fun onQueryChanged(query: String) {
        setState { it.copy(query = query) }
    }

    fun onSearchSubmitted() {
        val query = state.value.query.trim()
        if (query.isBlank()) {
            setState {
                it.copy(
                    hasSearched = false,
                    artists = emptyList(),
                    errorMessage = null,
                    isLoading = false,
                )
            }
            return
        }

        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            setState {
                it.copy(
                    isLoading = true,
                    hasSearched = true,
                    errorMessage = null,
                )
            }

            searchArtistsUseCase(
                ArtistSearchQueryModel(
                    query = query,
                    page = 1,
                    perPage = 20,
                )
            ).catch {
                setState {
                    it.copy(
                        isLoading = false,
                        artists = emptyList(),
                        errorMessage = "Failed to fetch artists. Please try again.",
                    )
                }
            }.collect { result ->
                setState {
                    it.copy(
                        isLoading = false,
                        artists = result.artists,
                        errorMessage = null,
                    )
                }
            }
        }
    }
}
