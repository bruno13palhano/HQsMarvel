package com.bruno13palhano.data.local.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bruno13palhano.data.local.data.RemoteKeysLocalData
import com.bruno13palhano.data.model.RemoteKeys

@Dao
internal interface RemoteKeysDao : RemoteKeysLocalData {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    override suspend fun insert(remoteKeys: RemoteKeys)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    override suspend fun insertAll(remoteKeys: List<RemoteKeys>)

    @Query("SELECT * FROM RemoteKeys WHERE comicId = :comicId")
    override suspend fun getRemoteKeyByComicId(comicId: Long): RemoteKeys?

    @Query("DELETE FROM RemoteKeys")
    override suspend fun clearRemoteKeys()

    @Query("DELETE FROM RemoteKeys WHERE comicId = :comicId")
    override suspend fun deleteById(comicId: Long)

    @Query("SELECT createdAt FROM remotekeys ORDER BY createdAt DESC LIMIT 1")
    override suspend fun getCreationTime(): Long?

    @Query("SELECT currentPage FROM RemoteKeys ORDER BY currentPage DESC LIMIT 1")
    override suspend fun getCurrentPage(): Int?
}