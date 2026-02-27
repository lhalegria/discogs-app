package com.example.discogsapp.main.viewmodel

import com.example.discogsapp.domain.model.ArtistSummaryModel
import com.example.discogsapp.viewmodel.flow.UIState

data class MainState(
    val query: String = "",
    val isLoading: Boolean = false,
    val hasSearched: Boolean = false,
    val artists: List<ArtistSummaryModel> = emptyList(),
    val errorMessage: String? = null,
) : UIState
