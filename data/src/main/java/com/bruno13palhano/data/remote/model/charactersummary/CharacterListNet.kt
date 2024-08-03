package com.bruno13palhano.data.remote.model.charactersummary

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CharacterListNet(
    @SerialName("items")
    val items: List<CharacterSummaryNet>
)