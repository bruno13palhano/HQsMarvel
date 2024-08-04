package com.bruno13palhano.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Entity(
    tableName = "CharacterSummary",
    foreignKeys = [
        ForeignKey(
            entity = Comic::class,
            parentColumns = ["id"],
            childColumns = ["comicId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class CharacterSummary(
    @PrimaryKey
    @SerialName("id")
    val id: Long,
    @SerialName("comicId")
    val comicId: Long,
    @SerialName("resourceURI")
    val resourceURI: String?,
    @SerialName("name")
    val name: String?,
    @SerialName("role")
    val role: String?
)