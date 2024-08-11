package com.bruno13palhano.data.remote.model

import androidx.annotation.Keep
import com.squareup.moshi.JsonClass

@Keep
@JsonClass(generateAdapter = true)
abstract class Data(open val id: Long)