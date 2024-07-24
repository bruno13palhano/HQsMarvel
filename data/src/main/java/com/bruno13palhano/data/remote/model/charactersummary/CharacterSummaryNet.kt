package com.bruno13palhano.data.remote.model.charactersummary

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CharacterSummaryNet(
    @SerialName("resourceURI")
    val resourceURI: String?,
    @SerialName("name")
    val name: String?,
    @SerialName("role")
    val role: String?
)