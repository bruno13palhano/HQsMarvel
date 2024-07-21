package com.bruno13palhano.data.model

import androidx.room.Entity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "Comics", primaryKeys = ["comicId"])
data class Comic(
    @SerialName("comicId")
    val comicId: Long,
    @SerialName("title")
    val title: String?,
    @SerialName("description")
    val description: String?,
    @SerialName("thumbnail")
    val thumbnail: String?,
    @SerialName("page")
    val page: Int,
    @SerialName("isFavorite")
    val isFavorite: Boolean
)