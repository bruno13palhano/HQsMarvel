package com.bruno13palhano.data.local.data

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import cache.FavoriteComicsQueries
import com.bruno13palhano.data.di.Dispatcher
import com.bruno13palhano.data.di.HQsMarvelDispatchers.IO
import com.bruno13palhano.data.model.Comic
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class DefaultFavoriteComicsLocalData
    @Inject
    constructor(
        private val favoriteComics: FavoriteComicsQueries,
        @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher
    ) : FavoriteComicsLocalData {
        override suspend fun saveFavorite(comic: Comic) {
            favoriteComics.saveFavorite(
                id = comic.id,
                title = comic.title,
                description = comic.description,
                thumbnail = comic.thumbnail
            )
        }

        override suspend fun removeFavorite(id: Long) {
            favoriteComics.removeFavorite(id = id)
        }

        override fun getComics(): Flow<List<Comic>> {
            return favoriteComics.getFavortieComics(mapper = ::mapToComic)
                .asFlow()
                .mapToList(ioDispatcher)
        }

        private fun mapToComic(
            id: Long,
            title: String,
            description: String,
            thumbnail: String
        ) = Comic(
            id = id,
            title = title,
            description = description,
            thumbnail = thumbnail
        )
    }