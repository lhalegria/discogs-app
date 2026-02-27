package com.example.discogsapp.main.viewmodel

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.discogsapp.domain.model.ArtistSummaryModel
import com.example.discogsapp.domain.usecase.artist.SearchArtistsUseCase
import com.example.discogsapp.main.viewmodel.paging.ArtistSearchPagingSource
import com.example.discogsapp.viewmodel.flow.StateViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update

@HiltViewModel
class MainViewModel @Inject constructor(
    private val searchArtistsUseCase: SearchArtistsUseCase,
) : StateViewModel<MainState>(MainState()) {

    private val searchQuery = MutableStateFlow<String?>(null)

    val artistsPagingData: Flow<PagingData<ArtistSummaryModel>> =
        searchQuery.flatMapLatest { query ->
            if (query == null) {
                emptyFlow()
            } else {
                Pager(
                    config = PagingConfig(
                        pageSize = PAGE_SIZE,
                        enablePlaceholders = false,
                    ),
                ) {
                    ArtistSearchPagingSource(
                        query = query,
                        searchArtistsUseCase = searchArtistsUseCase,
                    )
                }.flow
            }
        }.cachedIn(viewModelScope)

    fun onQueryChanged(query: String) {
        setState { it.copy(query = query) }
    }

    fun onSearchSubmitted() {
        val query = state.value.query.trim()
        if (query.isBlank()) {
            setState {
                it.copy(
                    hasSearched = false,
                )
            }
            searchQuery.update { null }
            return
        }

        setState {
            it.copy(
                hasSearched = true,
            )
        }

        searchQuery.update { query }
    }

    private companion object {
        const val PAGE_SIZE = 30
    }
}
