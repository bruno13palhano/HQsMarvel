package com.bruno13palhano.data.remote.model.charactersummary

import androidx.annotation.Keep
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class CharacterSummaryNet(
    @SerialName("resourceURI")
    val resourceURI: String?,
    @SerialName("name")
    val name: String?,
    @SerialName("role")
    val role: String?
)