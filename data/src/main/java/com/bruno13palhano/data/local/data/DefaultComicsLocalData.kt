package com.bruno13palhano.data.local.data

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import cache.ComicsQueries
import com.bruno13palhano.data.di.Dispatcher
import com.bruno13palhano.data.di.HQsMarvelDispatchers.IO
import com.bruno13palhano.data.model.Comic
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class DefaultFavoriteComicsLocalData
    @Inject
    constructor(
        private val favoriteComics: ComicsQueries,
        @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher
    ) : FavoriteComicsLocalData {
        override suspend fun save(comic: Comic) {
            favoriteComics.save(
                id = comic.id,
                title = comic.title,
                description = comic.description,
                thumbnail = comic.thumbnail,
                isFavorite = comic.isFavorite
            )
        }

        override suspend fun remove(id: Long) {
            favoriteComics.remove(id = id)
        }

        override fun getComics(): Flow<List<Comic>> {
            return favoriteComics.getFavoriteComics(mapper = ::mapToComic)
                .asFlow()
                .mapToList(ioDispatcher)
        }

        override suspend fun getAll(): List<Comic> {
            return favoriteComics.getAll(mapper = ::mapToComic).executeAsList()
        }

    override suspend fun updateFavorite(id: Long, isFavorite: Boolean) {
        favoriteComics.updateFavorite(id = id, isFavorite = isFavorite)
    }

    private fun mapToComic(
            id: Long,
            title: String,
            description: String,
            thumbnail: String,
            isFavorite: Boolean
        ) = Comic(
            id = id,
            title = title,
            description = description,
            thumbnail = thumbnail,
            isFavorite = isFavorite
        )
    }