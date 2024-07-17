package com.bruno13palhano.data.remote.model.comics

import com.bruno13palhano.data.remote.model.Thumbnail
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ComicNet(
    @Json(name = "id") val id: Long,
    @Json(name = "title") val title: String?,
    @Json(name = "description") val description: String?,
    @Json(name = "thumbnail") val thumbnail: Thumbnail?
)