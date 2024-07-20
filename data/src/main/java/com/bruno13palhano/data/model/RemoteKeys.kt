package com.bruno13palhano.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "RemoteKeys")
data class RemoteKeys(
    @PrimaryKey(autoGenerate = true)
    @SerialName("id")
    val id: Long = 0L,
    @SerialName("comicId")
    val comicId: Long,
    @SerialName("prevKey")
    val prevKey: Int?,
    @SerialName("currentPage")
    val currentPage: Int,
    @SerialName("nextKey")
    val nextKey: Int?,
    @SerialName("createAt")
    val createAt: Long
)