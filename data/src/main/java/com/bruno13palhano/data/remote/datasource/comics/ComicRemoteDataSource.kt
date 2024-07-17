package com.bruno13palhano.data.remote.datasource.comics

import com.bruno13palhano.data.model.Comic

internal interface ComicRemoteDataSource {
    suspend fun getComics(
        offset: Int,
        limit: Int
    ): List<Comic>
}