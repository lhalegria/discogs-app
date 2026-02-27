package com.example.discogsapp.detail.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    val artistId: Int = savedStateHandle.get<Int>("artistId") ?: 0
    val artistName: String = savedStateHandle.get<String>("artistName").orEmpty()
    val artistThumbnail: String = savedStateHandle.get<String>("artistThumbnail").orEmpty()
}
