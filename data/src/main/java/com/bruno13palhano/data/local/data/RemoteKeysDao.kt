package com.bruno13palhano.data.local.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bruno13palhano.data.model.RemoteKeys

@Dao
internal interface RemoteKeysDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(remoteKeys: RemoteKeys)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(remoteKeys: List<RemoteKeys>)

    @Query("SELECT * FROM RemoteKeys WHERE comicId = :comicId AND currentPage = :page")
    suspend fun remoteKeyId(
        comicId: Long,
        page: Int
    ): RemoteKeys?

    @Query("DELETE FROM RemoteKeys")
    suspend fun clearRemoteKeys()

    @Query("DELETE FROM RemoteKeys WHERE comicId = :comicId AND currentPage = :page")
    suspend fun deleteById(
        comicId: Long,
        page: Int
    )

    @Query("SELECT createAt FROM remotekeys ORDER BY createAt DESC LIMIT 1")
    suspend fun getCreationTime(): Long?

    @Query("SELECT currentPage FROM RemoteKeys ORDER BY currentPage DESC LIMIT 1")
    suspend fun getCurrentPage(): Int?
}