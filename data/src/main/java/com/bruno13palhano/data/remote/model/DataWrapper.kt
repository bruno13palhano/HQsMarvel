package com.bruno13palhano.data.remote.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DataWrapper<T : DataNet>(
    @Json(name = "code") val code: Int,
    @Json(name = "status") val status: String,
    @Json(name = "copyright") val copyright: String,
    @Json(name = "attributionText") val attributionText: String,
    @Json(name = "data") val data: DataContainer<T>,
    @Json(name = "etag") val etag: String
)