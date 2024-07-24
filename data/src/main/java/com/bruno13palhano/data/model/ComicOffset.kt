package com.bruno13palhano.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Entity to keeping track of the last offset.
 *
 * Because the server returns repeated comics from different pages,
 * and the comics that entered into the database are unique,
 * depending on the limit of the request all the data fetched may
 * already be in the database causing a loop of requests with the same data.
 * Therefore, it's necessary to keep track of the last offset to know where
 * to start the next request.
 *
 * @param id The id of this offset, it's always 1.
 *
 * @param lastOffset The last offset.
 */
@Serializable
@Entity(tableName = "ComicOffset")
data class ComicOffset(
    @SerialName("id")
    @PrimaryKey
    val id: Long,
    @SerialName("offset")
    val lastOffset: Int
)