package com.bruno13palhano.data.remote.model.comics

import androidx.annotation.Keep
import com.bruno13palhano.data.remote.model.Data
import com.bruno13palhano.data.remote.model.Thumbnail
import com.bruno13palhano.data.remote.model.charactersummary.CharacterListNet
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@Keep
@JsonClass(generateAdapter = true)
data class ComicNet(
    @Json(name = "id") override val id: Long,
    @Json(name = "title") val title: String?,
    @Json(name = "description") val description: String?,
    @Json(name = "thumbnail") val thumbnail: Thumbnail?,
    @Json(name = "characters") val characters: CharacterListNet
) : Data(id = id)