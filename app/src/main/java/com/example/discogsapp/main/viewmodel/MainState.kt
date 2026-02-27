package com.example.discogsapp.main.viewmodel

import com.example.discogsapp.viewmodel.flow.UIState

data class MainState(
    val query: String = "",
    val hasSearched: Boolean = false,
) : UIState
