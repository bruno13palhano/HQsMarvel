package com.bruno13palhano.data.repository.favorite

import com.bruno13palhano.data.model.Comic
import kotlinx.coroutines.flow.Flow

interface FavoriteComicsRepository {
    fun getComics(): Flow<List<Comic>>

    suspend fun saveFavorite(comic: Comic)

    suspend fun removeFavorite(id: Long)
}