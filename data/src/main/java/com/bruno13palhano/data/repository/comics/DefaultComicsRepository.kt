package com.bruno13palhano.data.repository.comics

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.bruno13palhano.data.local.data.ComicsDao
import com.bruno13palhano.data.local.database.HQsMarvelDatabase
import com.bruno13palhano.data.model.Comic
import com.bruno13palhano.data.remote.datasource.comics.ComicRemoteDataSource
import com.bruno13palhano.data.remote.di.DefaultComicRemote
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class DefaultComicsRepository
    @Inject
    constructor(
        @DefaultComicRemote private val comicRemoteDataSource: ComicRemoteDataSource,
        private val database: HQsMarvelDatabase,
        private val comicDao: ComicsDao
    ) : ComicsRepository {
        @OptIn(ExperimentalPagingApi::class)
        override fun getComics(): Flow<PagingData<Comic>> {
            return Pager(
                config = PagingConfig(pageSize = 15, enablePlaceholders = false),
                pagingSourceFactory = { comicDao.getAll() },
                remoteMediator =
                    ComicsRemoteMediator(
                        database = database,
                        comicRemoteDataSource = comicRemoteDataSource
                    )
            ).flow
        }

        override fun getFavoriteComicById(id: Long): Flow<Comic> {
            return comicDao.getFavoriteComicById(comicId = id)
        }

        override fun getFavoriteComics(): Flow<List<Comic>> {
            return comicDao.getFavoriteComics()
        }

        override suspend fun updateComicFavorite(
            id: Long,
            isFavorite: Boolean
        ) {
            return comicDao.updateFavorite(comicId = id, isFavorite = isFavorite)
        }
    }