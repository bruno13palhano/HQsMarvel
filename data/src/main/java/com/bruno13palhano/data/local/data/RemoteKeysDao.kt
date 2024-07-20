package com.bruno13palhano.data.local.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bruno13palhano.data.model.RemoteKeys

@Dao
internal interface RemoteKeysDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKeys: List<RemoteKeys>)

    @Query("SELECT * FROM RemoteKeys WHERE id = :id")
    suspend fun remoteKeyId(id: Long): RemoteKeys?

    @Query("DELETE FROM RemoteKeys")
    suspend fun clearRemoteKeys()

    @Query("SELECT createAt FROM remotekeys ORDER BY createAt DESC LIMIT 1")
    suspend fun getCreationTime(): Long?
}