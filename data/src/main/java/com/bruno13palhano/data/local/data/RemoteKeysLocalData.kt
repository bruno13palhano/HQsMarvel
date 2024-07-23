package com.bruno13palhano.data.local.data

import com.bruno13palhano.data.model.RemoteKeys

interface RemoteKeysLocalData {
    suspend fun insert(remoteKeys: RemoteKeys)

    suspend fun insertAll(remoteKeys: List<RemoteKeys>)

    suspend fun getRemoteKeyByComicId(comicId: Long): RemoteKeys?

    suspend fun clearRemoteKeys()

    suspend fun deleteById(comicId: Long)

    suspend fun getCreationTime(): Long?

    suspend fun getCurrentPage(): Int?
}