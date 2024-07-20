package com.bruno13palhano.data.repository.favorite

import com.bruno13palhano.data.local.data.FavoriteComicsDao
import com.bruno13palhano.data.model.FavoriteComic
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class DefaultFavoriteComicsRepository
    @Inject
    constructor(
        private val favoriteComics: FavoriteComicsDao
    ) : FavoriteComicsRepository {
        override fun getComics(): Flow<List<FavoriteComic>> {
            return favoriteComics.getAll()
        }

        override suspend fun saveFavorite(favoriteComic: FavoriteComic) {
            favoriteComics.insert(favoriteComic = favoriteComic)
        }

        override suspend fun removeFavorite(id: Long) {
            favoriteComics.delete(id = id)
        }
    }