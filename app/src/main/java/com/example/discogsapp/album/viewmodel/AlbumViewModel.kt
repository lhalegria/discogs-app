package com.example.discogsapp.album.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.discogsapp.domain.model.ArtistReleaseModel
import com.example.discogsapp.domain.model.ArtistReleasesQueryModel
import com.example.discogsapp.domain.usecase.artist.GetArtistReleasesUseCase
import com.example.discogsapp.viewmodel.flow.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlbumViewModel @Inject constructor(
    private val getArtistReleasesUseCase: GetArtistReleasesUseCase,
    private val dispatcher: CoroutineDispatcher,
) : ViewModel<AlbumState, AlbumEffect>(AlbumState()) {
    fun fetchArtistReleases(artistId: Int) {
        viewModelScope.launch {
            setState { it.copy(currentPage = 1, releases = emptyList(), hasMorePages = true) }
            loadReleasesForPage(artistId, page = 1, isInitialLoading = true)
        }
    }

    fun loadMore() {
        if (state.value.isLoadingMore || !state.value.hasMorePages) return
        viewModelScope.launch {
            val nextPage = state.value.currentPage + 1
            loadReleasesForPage(state.value.artistId, page = nextPage, isInitialLoading = false)
        }
    }

    private suspend fun loadReleasesForPage(
        artistId: Int,
        page: Int,
        isInitialLoading: Boolean,
    ) {
        getArtistReleasesUseCase(
            ArtistReleasesQueryModel(
                artistId = artistId,
                page = page,
                perPage = 30,
                sort = "year",
                sortOrder = "desc",
            ),
        ).onStart {
            setState {
                if (isInitialLoading) {
                    it.copy(artistId = artistId, isLoading = true, hasError = false)
                } else {
                    it.copy(isLoadingMore = true)
                }
            }
        }.catch {
            setState { it.copy(isLoading = false, isLoadingMore = false, hasError = true) }
        }.flowOn(dispatcher)
            .onCompletion {
                setState { it.copy(isLoading = false, isLoadingMore = false) }
            }.collect { result ->
                setState { currentState ->
                    val allReleases =
                        if (isInitialLoading) {
                            result.releases
                        } else {
                            currentState.releases + result.releases
                        }

                    val sortedReleases = allReleases.sortedByDescending { it.year }
                    val years = sortedReleases.map { it.year }.distinct().sorted()
                    val types = sortedReleases.map { it.type }.distinct().sorted()
                    val labels = sortedReleases.mapNotNull { it.label.ifEmpty { null } }.distinct().sorted()

                    val hasMorePages = page < result.pagination.pages

                    currentState.copy(
                        releases = sortedReleases,
                        totalAlbum = result.pagination.items,
                        filteredReleases =
                            applyAllFilters(
                                sortedReleases,
                                currentState.selectedType,
                                currentState.selectedYear,
                                currentState.selectedLabel,
                            ),
                        availableYears = years,
                        availableTypes = types,
                        availableLabels = labels,
                        currentPage = page,
                        hasMorePages = hasMorePages,
                        hasError = false,
                    )
                }
            }
    }

    fun filterByType(type: String) {
        setState {
            val filtered =
                if (type.isEmpty()) {
                    it.releases
                } else {
                    it.releases.filter { release -> release.type == type }
                }

            it.copy(
                selectedType = type,
                filteredReleases =
                    applyAllFilters(
                        filtered,
                        type,
                        it.selectedYear,
                        it.selectedLabel,
                    ),
            )
        }
    }

    fun filterByYear(year: String) {
        setState {
            val filtered =
                if (year.isEmpty()) {
                    it.releases
                } else {
                    it.releases.filter { release -> release.year == year }
                }

            it.copy(
                selectedYear = year,
                filteredReleases =
                    applyAllFilters(
                        filtered,
                        it.selectedType,
                        year,
                        it.selectedLabel,
                    ),
            )
        }
    }

    fun filterByLabel(label: String) {
        setState {
            val filtered =
                if (label.isEmpty()) {
                    it.releases
                } else {
                    it.releases.filter { release -> release.label == label }
                }

            it.copy(
                selectedLabel = label,
                filteredReleases =
                    applyAllFilters(
                        filtered,
                        it.selectedType,
                        it.selectedYear,
                        label,
                    ),
            )
        }
    }

    fun clearFilters() {
        setState {
            it.copy(
                selectedType = "",
                selectedYear = "",
                selectedLabel = "",
                filteredReleases = it.releases,
            )
        }
    }

    fun retry() {
        fetchArtistReleases(state.value.artistId)
    }

    private fun applyAllFilters(
        releases: List<ArtistReleaseModel>,
        type: String,
        year: String,
        label: String,
    ): List<ArtistReleaseModel> {
        var filtered = releases

        if (year.isNotEmpty()) {
            filtered = filtered.filter { it.year == year }
        }

        if (type.isNotEmpty()) {
            filtered = filtered.filter { it.type == type }
        }

        if (label.isNotEmpty()) {
            filtered = filtered.filter { it.label == label }
        }

        return filtered.sortedByDescending { it.year }
    }
}
