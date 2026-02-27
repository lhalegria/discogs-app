package com.example.discogsapp.main.viewmodel.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.discogsapp.domain.model.ArtistSearchQueryModel
import com.example.discogsapp.domain.model.ArtistSummaryModel
import com.example.discogsapp.domain.usecase.artist.SearchArtistsUseCase
import kotlinx.coroutines.flow.first

class ArtistSearchPagingSource(
    private val query: String,
    private val searchArtistsUseCase: SearchArtistsUseCase,
) : PagingSource<Int, ArtistSummaryModel>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ArtistSummaryModel> {
        val currentPage = params.key ?: 1

        return try {
            val result = searchArtistsUseCase(
                ArtistSearchQueryModel(
                    query = query,
                    page = currentPage,
                    perPage = params.loadSize,
                ),
            ).first()

            val nextPage = if (currentPage >= result.pagination.pages) {
                null
            } else {
                currentPage + 1
            }

            LoadResult.Page(
                data = result.artists,
                prevKey = if (currentPage == 1) null else currentPage - 1,
                nextKey = nextPage,
            )
        } catch (throwable: Throwable) {
            LoadResult.Error(throwable)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ArtistSummaryModel>): Int? =
        state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
}
