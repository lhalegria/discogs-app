package com.example.discogsapp.detail.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor() : ViewModel() {
    var artistId: Int = 0
        private set

    var artistName: String = ""
        private set

    var artistThumbnail: String = ""
        private set

    fun bind(
        artistId: Int,
        artistName: String,
        artistThumbnail: String,
    ) {
        this.artistId = artistId
        this.artistName = artistName
        this.artistThumbnail = artistThumbnail
    }
}
