package com.bruno13palhano.data.remote.model

import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@Keep
@JsonClass(generateAdapter = true)
data class DataContainer<T : Data>(
    @Json(name = "results") val results: List<T>
)