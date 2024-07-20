package com.bruno13palhano.data.repository.favorite

import com.bruno13palhano.data.model.FavoriteComic
import kotlinx.coroutines.flow.Flow

interface FavoriteComicsRepository {
    fun getComics(): Flow<List<FavoriteComic>>

    suspend fun saveFavorite(favoriteComic: FavoriteComic)

    suspend fun removeFavorite(id: Long)
}