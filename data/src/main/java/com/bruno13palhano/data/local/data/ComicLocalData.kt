package com.bruno13palhano.data.local.data

import androidx.paging.PagingSource
import com.bruno13palhano.data.model.Comic
import kotlinx.coroutines.flow.Flow

internal interface ComicLocalData {
    suspend fun insertComic(comic: Comic)

    suspend fun insertComics(comics: List<Comic>)

    fun getComicsPaging(): PagingSource<Int, Comic>

    suspend fun clearComics()

    suspend fun getComics(): List<Comic>

    fun getFavoriteComics(): Flow<List<Comic>>

    fun getFavoriteComicById(comicId: Long): Flow<Comic>

    suspend fun getNextPageByComicId(id: Long): Int?

    suspend fun getCreationTime(): Long?

    suspend fun updateFavorite(
        comicId: Long,
        isFavorite: Boolean
    )
}