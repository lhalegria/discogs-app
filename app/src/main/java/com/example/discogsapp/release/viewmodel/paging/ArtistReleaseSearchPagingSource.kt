package com.example.discogsapp.release.viewmodel.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.discogsapp.domain.model.ArtistReleaseModel
import com.example.discogsapp.domain.model.ArtistReleaseQueryModel
import com.example.discogsapp.domain.usecase.release.SearchArtistReleaseUseCase
import kotlinx.coroutines.flow.first
import java.io.IOException
import kotlin.coroutines.cancellation.CancellationException

class ArtistReleaseSearchPagingSource(
    private val artist: String,
    private val year: String?,
    private val genre: String?,
    private val label: String?,
    private val pageSize: Int,
    private val searchArtistReleaseUseCase: SearchArtistReleaseUseCase,
) : PagingSource<Int, ArtistReleaseModel>() {
    private val seenKeys = HashSet<String>()

    override fun getRefreshKey(state: PagingState<Int, ArtistReleaseModel>): Int? =
        state.anchorPosition?.let { pos ->
            state.closestPageToPosition(pos)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(pos)?.nextKey?.minus(1)
        }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ArtistReleaseModel> {
        val currentPage = params.key ?: 1

        return try {
            val result =
                searchArtistReleaseUseCase(
                    ArtistReleaseQueryModel(
                        page = currentPage,
                        perPage = pageSize,
                        artist = artist,
                        year = year,
                        genre = genre,
                        label = label,
                    ),
                ).first()

            val unique =
                result.releases.filter { item ->
                    val identityKey = "${item.type}:${item.id}"
                    seenKeys.add(identityKey)
                }

            LoadResult.Page(
                data = unique,
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
