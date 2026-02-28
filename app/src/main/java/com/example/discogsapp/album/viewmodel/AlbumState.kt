package com.example.discogsapp.album.viewmodel

import com.example.discogsapp.domain.model.ArtistReleaseModel
import com.example.discogsapp.viewmodel.flow.UIState

data class AlbumState(
    val artistId: Int = 0,
    val totalAlbum: Int = 0,
    val releases: List<ArtistReleaseModel> = emptyList(),
    val filteredReleases: List<ArtistReleaseModel> = emptyList(),
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val hasError: Boolean = false,
    val selectedYear: String = "",
    val selectedType: String = "",
    val selectedLabel: String = "",
    val availableYears: List<String> = emptyList(),
    val availableTypes: List<String> = emptyList(),
    val availableLabels: List<String> = emptyList(),
    val currentPage: Int = 1,
    val hasMorePages: Boolean = true,
) : UIState {
    fun isInitialLoading() = isLoading && releases.isEmpty()

    fun isSuccessful() = releases.isNotEmpty()

    fun isError() = hasError
}
