package com.bruno13palhano.data.local.data

import com.bruno13palhano.data.model.Comic
import kotlinx.coroutines.flow.Flow

interface FavoriteComicsLocalData {
    suspend fun saveFavorite(comic: Comic)

    suspend fun removeFavorite(id: Long)

    fun getComics(): Flow<List<Comic>>
}