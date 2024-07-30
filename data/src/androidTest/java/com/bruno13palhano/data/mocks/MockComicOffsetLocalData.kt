package com.bruno13palhano.data.mocks

import com.bruno13palhano.data.local.data.ComicOffsetLocalData
import com.bruno13palhano.data.model.ComicOffset

class MockComicOffsetLocalData : ComicOffsetLocalData {
    private var lastOffset: ComicOffset? = null

    override suspend fun insert(comicOffset: ComicOffset) {
        lastOffset = comicOffset
    }

    override suspend fun getLastOffset(): Int? {
        return lastOffset?.lastOffset
    }
}