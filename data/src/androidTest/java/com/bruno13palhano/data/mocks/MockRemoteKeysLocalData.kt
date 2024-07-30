package com.bruno13palhano.data.mocks

import com.bruno13palhano.data.local.data.RemoteKeysLocalData
import com.bruno13palhano.data.model.RemoteKeys

class MockRemoteKeysLocalData : RemoteKeysLocalData {
    private val keys = mutableListOf<RemoteKeys>()

    override suspend fun insert(remoteKeys: RemoteKeys) {
        keys.add(remoteKeys)
    }

    override suspend fun insertAll(remoteKeys: List<RemoteKeys>) {
        keys.addAll(remoteKeys)
    }

    override suspend fun getRemoteKeyByComicId(comicId: Long): RemoteKeys? {
        return keys.find { it.comicId == comicId }
    }

    override suspend fun clearRemoteKeys() {
        keys.clear()
    }

    override suspend fun deleteById(comicId: Long) {
        keys.removeIf { it.comicId == comicId }
    }

    override suspend fun getCreationTime(): Long? {
        return keys.lastOrNull()?.createdAt
    }

    override suspend fun getCurrentPage(): Int? {
        return keys.lastOrNull()?.currentPage
    }
}