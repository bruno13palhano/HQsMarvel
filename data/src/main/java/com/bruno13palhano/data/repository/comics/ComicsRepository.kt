package com.bruno13palhano.data.repository.comics

import androidx.paging.PagingData
import com.bruno13palhano.data.model.Comic
import kotlinx.coroutines.flow.Flow

interface ComicsRepository {
    fun getComics(): Flow<PagingData<Comic>>

    fun getFavoriteComicById(
        id: Long,
        page: Int
    ): Flow<Comic>

    fun getFavoriteComics(): Flow<List<Comic>>

    suspend fun updateComicFavorite(
        id: Long,
        page: Int,
        isFavorite: Boolean
    )
}