package com.bruno13palhano.data.model

import androidx.room.Entity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "RemoteKeys", primaryKeys = ["comicId"])
data class RemoteKeys(
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