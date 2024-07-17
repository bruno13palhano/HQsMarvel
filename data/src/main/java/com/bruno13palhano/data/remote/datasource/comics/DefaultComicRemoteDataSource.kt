package com.bruno13palhano.data.remote.datasource.comics

import com.bruno13palhano.data.model.Comic
import com.bruno13palhano.data.remote.Service
import javax.inject.Inject

internal class DefaultComicRemoteDataSource
    @Inject
    constructor(
        private val service: Service
    ) : ComicRemoteDataSource {
        override suspend fun getComics(
            offset: Int,
            limit: Int
        ): List<Comic> {
            return service.getComics(offset = offset, limit = limit).data.results.map { comic ->
                Comic(
                    id = comic.id,
                    title = comic.title ?: "",
                    description = comic.description ?: "",
                    thumbnail =
                        (comic.thumbnail?.path ?: "") + "." + (comic.thumbnail?.extension ?: "")
                )
            }
        }
    }