package com.bruno13palhano.data.local.data

import com.bruno13palhano.data.model.Comic
import com.bruno13palhano.data.model.RemoteKeys

internal interface MediatorComicLocalData {
    suspend fun insertAll(
        page: Int,
        endOfPaginationReached: Boolean,
        isRefresh: Boolean,
        comics: List<Comic>
    )

    suspend fun getRemoteKeyByComicId(comicId: Long): RemoteKeys?

    suspend fun getCreationTime(): Long?

    suspend fun getCurrentPage(): Int?
}