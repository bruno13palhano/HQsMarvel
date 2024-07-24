package com.bruno13palhano.data.repository.character

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.bruno13palhano.data.local.data.CharacterSummaryLocalData
import com.bruno13palhano.data.model.CharacterSummary
import retrofit2.HttpException

internal class CharacterSummaryPagingSource(
    private val characterSummaryLocalData: CharacterSummaryLocalData,
    private val comicId: Long,
    private var offset: Int = 0,
    private val limit: Int = 15
) : PagingSource<Int, CharacterSummary>() {
    override fun getRefreshKey(state: PagingState<Int, CharacterSummary>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CharacterSummary> {
        return try {
            val currentPage = params.key ?: 1
            val response =
                characterSummaryLocalData.getCharacterSummaryByComicId(
                    comicId = comicId,
                    offset = offset,
                    limit = limit
                )

            offset += limit

            LoadResult.Page(
                data = response,
                prevKey = if (currentPage == 1) null else currentPage - 1,
                nextKey = if (response.isEmpty()) null else currentPage + 1
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        } catch (e: HttpException) {
            return LoadResult.Error(e)
        }
    }
}