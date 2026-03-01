package com.example.discogsapp.album.viewmodel

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.discogsapp.album.viewmodel.paging.ArtistReleaseSearchPagingSource
import com.example.discogsapp.domain.model.ArtistReleaseModel
import com.example.discogsapp.domain.usecase.release.SearchArtistReleaseUseCase
import com.example.discogsapp.viewmodel.flow.StateViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import kotlin.String

@HiltViewModel
class ReleaseViewModel @Inject constructor(
    private val searchArtistReleaseUseCase: SearchArtistReleaseUseCase,
) : StateViewModel<ReleaseState>(ReleaseState()) {
    private val searchQuery = MutableStateFlow<String>("")

    private var artist: String = ""

    @OptIn(ExperimentalCoroutinesApi::class)
    val artistReleasePagingData: Flow<PagingData<ArtistReleaseModel>> =
        searchQuery
            .flatMapLatest {
                Pager(
                    config =
                        PagingConfig(
                            pageSize = PAGE_SIZE,
                            enablePlaceholders = false,
                        ),
                ) {
                    ArtistReleaseSearchPagingSource(
                        artist = artist,
                        year = if (state.value.isYearFilter) state.value.query else null,
                        genre = if (state.value.isGenreFilter) state.value.query else null,
                        label = if (state.value.isLabelFilter) state.value.query else null,
                        pageSize = PAGE_SIZE,
                        searchArtistReleaseUseCase = searchArtistReleaseUseCase,
                    )
                }.flow
            }.cachedIn(viewModelScope)

    fun onQueryChanged(query: String) {
        setState { it.copy(query = query) }
        searchQuery.update { query.trim() }
    }

    fun onSearchSubmitted() {
        onQueryChanged(state.value.query)
    }

    fun setYearFilter() {
        setState { it.copy(isYearFilter = true, isGenreFilter = false, isLabelFilter = false, query = "") }
        onQueryChanged(state.value.query)
    }

    fun setGenreFilter() {
        setState { it.copy(isGenreFilter = true, isYearFilter = false, isLabelFilter = false, query = "") }
        onQueryChanged(state.value.query)
    }

    fun setLabelFilter() {
        setState { it.copy(isLabelFilter = true, isGenreFilter = false, isYearFilter = false, query = "") }
        onQueryChanged(state.value.query)
    }

    fun setArtist(artist: String) {
        this.artist = artist
    }

    private companion object {
        const val PAGE_SIZE = 30
    }
}
