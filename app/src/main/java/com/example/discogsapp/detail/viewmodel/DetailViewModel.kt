package com.example.discogsapp.detail.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.discogsapp.navigation.AppRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    val artistId: Int = savedStateHandle.get<Int>(AppRoute.Detail.artistIdArg) ?: 0
    val artistName: String = savedStateHandle.get<String>(AppRoute.Detail.artistNameArg).orEmpty()
    val artistThumbnail: String = savedStateHandle.get<String>(AppRoute.Detail.artistThumbnailArg).orEmpty()
}
