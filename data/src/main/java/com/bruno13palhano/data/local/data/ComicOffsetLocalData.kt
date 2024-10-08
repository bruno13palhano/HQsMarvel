package com.bruno13palhano.data.local.data

import com.bruno13palhano.data.model.ComicOffset

interface ComicOffsetLocalData {
    suspend fun insertComicOffset(comicOffset: ComicOffset)

    suspend fun getLastOffset(): Int?
}