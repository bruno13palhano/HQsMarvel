package com.bruno13palhano.data.mocks

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.bruno13palhano.data.local.data.ComicLocalData
import com.bruno13palhano.data.model.Comic
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf

class MockComicLocalData : ComicLocalData {
    private val comics = mutableListOf<Comic>()

    override suspend fun insert(comic: Comic) {
        if (!comics.contains(comic)) {
            comics.add(comic)
        }
    }

    override suspend fun insertAll(comics: List<Comic>) {
        val tmp = mutableListOf<Comic>()
        comics.forEach {
            if (!this.comics.contains(it)) {
                tmp.add(it)
            }
        }
        this.comics.addAll(tmp)
    }

    override fun getAll(): PagingSource<Int, Comic> {
        return object : PagingSource<Int, Comic>() {
            override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Comic> {
                val currentPage = params.key ?: 1
                val response =
                    comics.subList(
                        (currentPage - 1) * params.loadSize,
                        currentPage * params.loadSize * 3
                    )

                return LoadResult.Page(
                    data = emptyList(),
                    prevKey = null,
                    nextKey = null
                )
            }

            override fun getRefreshKey(state: PagingState<Int, Comic>): Int? {
                return state.anchorPosition
            }
        }
    }

    override suspend fun clearComics() {
        comics.removeAll { !it.isFavorite }
    }

    override suspend fun getComics(): List<Comic> {
        return comics.sortedBy { it.page }
    }

    override fun getFavoriteComics(): Flow<List<Comic>> {
        return flowOf(comics.filter { it.isFavorite })
    }

    override fun getFavoriteComicById(comicId: Long): Flow<Comic> {
        return try {
            flowOf(comics.find { it.comicId == comicId }!!)
        } catch (e: Exception) {
            emptyFlow()
        }
    }

    override suspend fun updateFavorite(
        comicId: Long,
        isFavorite: Boolean
    ) {
        comics.find { it.comicId == comicId }?.let {
            comics[comics.indexOf(it)] = it.copy(isFavorite = isFavorite)
        }
    }
}