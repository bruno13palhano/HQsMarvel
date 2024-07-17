package com.bruno13palhano.data.repository.favorite

import com.bruno13palhano.data.local.data.FavoriteComicsLocalData
import com.bruno13palhano.data.local.di.FavoriteComicsLocal
import com.bruno13palhano.data.model.Comic
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class DefaultFavoriteComicsRepository
    @Inject
    constructor(
        @FavoriteComicsLocal private val favoriteComicsLocal: FavoriteComicsLocalData
    ) : FavoriteComicsRepository {
        override fun getComics(): Flow<List<Comic>> {
            return favoriteComicsLocal.getComics()
        }

        override suspend fun saveFavorite(comic: Comic) {
            favoriteComicsLocal.saveFavorite(comic = comic)
        }

        override suspend fun removeFavorite(id: Long) {
            favoriteComicsLocal.removeFavorite(id = id)
        }
    }