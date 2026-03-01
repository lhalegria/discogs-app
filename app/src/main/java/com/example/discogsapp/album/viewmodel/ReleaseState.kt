package com.example.discogsapp.album.viewmodel

import com.example.discogsapp.viewmodel.flow.UIState

data class ReleaseState(
    val query: String = "",
    val isYearFilter: Boolean = false,
    val isGenreFilter: Boolean = false,
    val isLabelFilter: Boolean = false,
) : UIState
