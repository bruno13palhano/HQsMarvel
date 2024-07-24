package com.bruno13palhano.data.local.data

import com.bruno13palhano.data.model.ComicOffset
import com.bruno13palhano.data.model.RemoteKeys
import com.bruno13palhano.data.remote.model.comics.ComicNet

internal interface MediatorComicLocalData {
    suspend fun insertAll(
        page: Int,
        nextOffset: Int,
        endOfPaginationReached: Boolean,
        isRefresh: Boolean,
        comics: List<ComicNet>
    )

    suspend fun insertLastOffset(lastOffset: ComicOffset)

    suspend fun getRemoteKeyByComicId(comicId: Long): RemoteKeys?

    suspend fun getCreationTime(): Long?

    suspend fun getLastOffset(): Int?
}