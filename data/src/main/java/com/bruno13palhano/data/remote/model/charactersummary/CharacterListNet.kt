package com.bruno13palhano.data.remote.model.charactersummary

import androidx.annotation.Keep
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class CharacterListNet(
    @SerialName("items")
    val items: List<CharacterSummaryNet>
)