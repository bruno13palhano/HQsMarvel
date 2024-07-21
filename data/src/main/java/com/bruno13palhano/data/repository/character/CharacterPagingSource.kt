package com.bruno13palhano.data.repository.character

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.bruno13palhano.data.model.Character
import com.bruno13palhano.data.remote.datasource.character.CharacterRemoteDataSource
import retrofit2.HttpException

internal class CharacterPagingSource(
    private val characterRemoteDataSource: CharacterRemoteDataSource,
    private val id: Long,
    private var offset: Int = 0,
    private val limit: Int = 15
) : PagingSource<Int, Character>() {
    override fun getRefreshKey(state: PagingState<Int, Character>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Character> {
        return try {
            val currentPage = params.key ?: 1
            val response = characterRemoteDataSource.getCharacters(id = id, offset = offset, limit = limit)

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