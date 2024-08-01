package com.bruno13palhano.data.remote.model.character

import com.bruno13palhano.data.remote.model.DataNet
import com.bruno13palhano.data.remote.model.Thumbnail
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CharacterNet(
    @Json(name = "id") override val id: Long,
    @Json(name = "name") val name: String?,
    @Json(name = "description") val description: String?,
    @Json(name = "thumbnail") val thumbnail: Thumbnail?
) : DataNet(id = id)