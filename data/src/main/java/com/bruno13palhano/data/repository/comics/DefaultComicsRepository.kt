package com.bruno13palhano.data.repository.comics

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.bruno13palhano.data.model.Comic
import com.bruno13palhano.data.remote.datasource.comics.ComicRemoteDataSource
import com.bruno13palhano.data.remote.di.DefaultComicRemote
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class DefaultComicsRepository
    @Inject
    constructor(
        @DefaultComicRemote private val comicRemoteDataSource: ComicRemoteDataSource
    ) : ComicsRepository {
        override fun getComics(): Flow<PagingData<Comic>> {
            return Pager(
                config = PagingConfig(pageSize = 15, prefetchDistance = 1),
                pagingSourceFactory = {
                    ComicsPagingSource(
                        comicRemoteDataSource = comicRemoteDataSource,
                        offset = 0,
                        limit = 15
                    )
                }
            ).flow
        }
    }