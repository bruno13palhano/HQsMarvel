package com.bruno13palhano.data.remote.model

import androidx.annotation.Keep

@Keep
data class Thumbnail(
    val path: String?,
    val extension: String?
)