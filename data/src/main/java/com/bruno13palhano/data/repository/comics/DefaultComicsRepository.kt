package com.bruno13palhano.data.repository.comics

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.bruno13palhano.data.local.data.ComicLocalData
import com.bruno13palhano.data.local.data.MediatorComicLocalData
import com.bruno13palhano.data.local.di.DefaultComic
import com.bruno13palhano.data.local.di.DefaultComicMediator
import com.bruno13palhano.data.model.Comic
import com.bruno13palhano.data.remote.datasource.comics.ComicRemote
import com.bruno13palhano.data.remote.di.ComicRemoteSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class DefaultComicsRepository
    @Inject
    constructor(
        @ComicRemoteSource private val comicRemote: ComicRemote,
        @DefaultComicMediator private val mediatorComicLocalData: MediatorComicLocalData,
        @DefaultComic private val comicLocalData: ComicLocalData
    ) : ComicsRepository {
        @OptIn(ExperimentalPagingApi::class)
        override fun getComics(): Flow<PagingData<Comic>> {
            return Pager(
                config = PagingConfig(pageSize = 15, enablePlaceholders = false),
                pagingSourceFactory = { comicLocalData.getComicsPaging() },
                remoteMediator =
                    ComicsRemoteMediator(
                        mediatorComicLocalData = mediatorComicLocalData,
                        comicRemote = comicRemote
                    )
            ).flow
        }

        override fun getFavoriteComicById(id: Long): Flow<Comic> {
            return comicLocalData.getFavoriteComicById(comicId = id)
        }

        override fun getFavoriteComics(): Flow<List<Comic>> {
            return comicLocalData.getFavoriteComics()
        }

        override suspend fun updateComicFavorite(
            id: Long,
            isFavorite: Boolean
        ) {
            return comicLocalData.updateFavorite(comicId = id, isFavorite = isFavorite)
        }
    }