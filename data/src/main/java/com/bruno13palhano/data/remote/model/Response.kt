package com.bruno13palhano.data.remote.model

import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@Keep
@JsonClass(generateAdapter = true)
data class Response<T : Data>(
    @Json(name = "copyright") val copyright: String,
    @Json(name = "attributionText") val attributionText: String,
    @Json(name = "data") val data: DataContainer<T>
)