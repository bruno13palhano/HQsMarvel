package com.bruno13palhano.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "Comics")
data class Comic(
    @PrimaryKey(autoGenerate = true)
    @SerialName("id")
    val id: Long = 0L,
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