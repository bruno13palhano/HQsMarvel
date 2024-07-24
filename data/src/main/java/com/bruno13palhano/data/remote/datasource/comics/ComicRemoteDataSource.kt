package com.bruno13palhano.data.remote.datasource.comics

import com.bruno13palhano.data.remote.model.comics.ComicNet

internal interface ComicRemoteDataSource {
    suspend fun getComics(
        offset: Int,
        limit: Int
    ): List<ComicNet>
}