package com.bruno13palhano.data.repository.comics

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.bruno13palhano.data.model.Comic
import com.bruno13palhano.data.remote.datasource.comics.ComicRemoteDataSource
import retrofit2.HttpException

internal class ComicsPagingSource(
    private val comicRemoteDataSource: ComicRemoteDataSource,
    private var offset: Int = 0,
    private val limit: Int = 15
) : PagingSource<Int, Comic>() {
    override fun getRefreshKey(state: PagingState<Int, Comic>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Comic> {
        return try {
            val currentPage = params.key ?: 1
            val response = comicRemoteDataSource.getComics(offset = offset, limit = limit)

            offset += limit

            LoadResult.Page(
                data = response,
                prevKey = if (currentPage == 1) null else currentPage - 1,
                nextKey = if (response.isEmpty()) null else currentPage + 1
            )
        } catch (e: Exception) {
            println(e)
            return LoadResult.Error(e)
        } catch (e: HttpException) {
            println(e)
            return LoadResult.Error(e)
        }
    }
}