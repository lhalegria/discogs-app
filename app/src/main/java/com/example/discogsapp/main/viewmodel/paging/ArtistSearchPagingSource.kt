package com.example.discogsapp.main.viewmodel.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.discogsapp.domain.model.ArtistQueryModel
import com.example.discogsapp.domain.model.ArtistSummaryModel
import com.example.discogsapp.domain.usecase.artist.SearchArtistUseCase
import kotlinx.coroutines.flow.first
import java.io.IOException
import kotlin.coroutines.cancellation.CancellationException

class ArtistSearchPagingSource(
    private val query: String,
    private val pageSize: Int,
    private val searchArtistUseCase: SearchArtistUseCase,
) : PagingSource<Int, ArtistSummaryModel>() {
    override fun getRefreshKey(state: PagingState<Int, ArtistSummaryModel>): Int? =
        state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ArtistSummaryModel> {
        val currentPage = params.key ?: 1

        return try {
            val result =
                searchArtistUseCase(
                    ArtistQueryModel(
                        query = query,
                        page = currentPage,
                        perPage = pageSize,
                    ),
                ).first()

            LoadResult.Page(
                data = result.artists,
                prevKey = if (currentPage == 1) null else currentPage - 1,
                nextKey = if (currentPage >= result.pagination.pages) null else currentPage + 1,
            )
        } catch (e: CancellationException) {
            throw e
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}
