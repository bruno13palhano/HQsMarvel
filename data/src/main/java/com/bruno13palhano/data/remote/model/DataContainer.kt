package com.bruno13palhano.data.remote.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DataContainer<T : DataNet>(
    @Json(name = "offset") val offset: Int,
    @Json(name = "limit") val limit: Int,
    @Json(name = "total") val total: Int,
    @Json(name = "count") val count: Int,
    @Json(name = "results") val results: List<T>
)