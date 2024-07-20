package com.bruno13palhano.data.repository.comics

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.bruno13palhano.data.local.data.ComicsDao
import com.bruno13palhano.data.local.data.FavoriteComicsDao
import com.bruno13palhano.data.local.database.HQsMarvelDatabase
import com.bruno13palhano.data.model.Comic
import com.bruno13palhano.data.model.FavoriteComic
import com.bruno13palhano.data.remote.datasource.comics.ComicRemoteDataSource
import com.bruno13palhano.data.remote.di.DefaultComicRemote
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class DefaultComicsRepository
    @Inject
    constructor(
        @DefaultComicRemote private val comicRemoteDataSource: ComicRemoteDataSource,
        private val database: HQsMarvelDatabase,
        private val comicDao: ComicsDao,
        private val favoriteComicsDao: FavoriteComicsDao
    ) : ComicsRepository {
        @OptIn(ExperimentalPagingApi::class)
        override fun getComics(): Flow<PagingData<Comic>> {
            return Pager(
                config = PagingConfig(pageSize = 15, prefetchDistance = 0, initialLoadSize = 15),
                pagingSourceFactory = { comicDao.getAll() },
                remoteMediator =
                    ComicsRemoteMediator(
                        database = database,
                        comicRemoteDataSource = comicRemoteDataSource
                    )
            ).flow
        }

        override suspend fun addToFavorite(comic: Comic) {
            favoriteComicsDao.insert(
                favoriteComic =
                    FavoriteComic(
                        id = comic.id,
                        title = comic.title,
                        description = comic.description,
                        thumbnail = comic.thumbnail
                    )
            )
        }
    }