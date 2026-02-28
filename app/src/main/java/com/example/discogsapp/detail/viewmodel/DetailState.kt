package com.example.discogsapp.detail.viewmodel

import com.example.discogsapp.domain.model.ArtistDetailsModel
import com.example.discogsapp.viewmodel.flow.UIState

data class DetailState(
    val artistId: Int = 0,
    val artistInfo: ArtistDetailsModel? = null,
    val isLoading: Boolean = false,
    val hasError: Boolean = false,
) : UIState {
    fun isError() = hasError

    fun isSuccessful() = artistInfo != null
}
