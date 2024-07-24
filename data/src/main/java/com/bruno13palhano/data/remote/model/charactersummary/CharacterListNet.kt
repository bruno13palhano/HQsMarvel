package com.bruno13palhano.data.remote.model.charactersummary

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CharacterListNet(
    @SerialName("available")
    val available: Int,
    @SerialName("returned")
    val returned: Int,
    @SerialName("collectionURI")
    val collectionURI: String?,
    @SerialName("items")
    val items: List<CharacterSummaryNet>
)