package com.example.discogsapp.detail.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.discogsapp.album.navigation.AlbumRoute
import com.example.discogsapp.domain.usecase.artist.GetArtistDetailsUseCase
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
class DetailViewModel @Inject constructor(
    private val getArtistDetailsUseCase: GetArtistDetailsUseCase,
    private val dispatcher: CoroutineDispatcher,
) : ViewModel<DetailState, DetailEffect>(DetailState()) {
    fun fetchArtistDetails(artistId: Int) {
        viewModelScope.launch {
            getArtistDetailsUseCase(artistId)
                .onStart {
                    setState {
                        it.copy(
                            artistId = artistId,
                            isLoading = true,
                            hasError = false,
                        )
                    }
                }.catch {
                    setState { it.copy(isLoading = false, hasError = true) }
                }.flowOn(dispatcher)
                .onCompletion { setState { it.copy(isLoading = false) } }
                .collect { artist ->
                    setState {
                        it.copy(
                            artistInfo = artist,
                            hasError = false,
                        )
                    }
                }
        }
    }

    fun retry() {
        fetchArtistDetails(state.value.artistId)
    }

    fun navigateToAlbum(albumId: Int) {
        sendEffect(DetailEffect.NavigateToAlbum(AlbumRoute(albumId)))
    }

    fun openUrl(url: String) {
        sendEffect(DetailEffect.OpenUrl(url))
    }
}
