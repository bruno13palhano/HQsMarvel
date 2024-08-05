package com.bruno13palhano.data.remote.datasource.comics

import com.bruno13palhano.data.remote.model.Response
import com.bruno13palhano.data.remote.model.comics.ComicNet

internal interface ComicRemote {
    suspend fun getComics(
        offset: Int,
        limit: Int
    ): Response<ComicNet>
}