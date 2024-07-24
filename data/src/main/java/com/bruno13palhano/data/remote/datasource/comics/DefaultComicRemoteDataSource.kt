package com.bruno13palhano.data.remote.datasource.comics

import com.bruno13palhano.data.remote.Service
import com.bruno13palhano.data.remote.model.comics.ComicNet
import javax.inject.Inject

internal class DefaultComicRemoteDataSource
    @Inject
    constructor(
        private val service: Service
    ) : ComicRemoteDataSource {
        override suspend fun getComics(
            offset: Int,
            limit: Int
        ): List<ComicNet> {
            return service.getComics(offset = offset, limit = limit).data.results
        }
    }