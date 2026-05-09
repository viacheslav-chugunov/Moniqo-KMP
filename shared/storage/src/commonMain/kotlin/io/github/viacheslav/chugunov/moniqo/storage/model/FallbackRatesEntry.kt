package io.github.viacheslav.chugunov.moniqo.storage.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class FallbackRatesEntry(
    @SerialName("date")
    val date: String,
    @SerialName("eur")
    val eur: Map<String, Double>,
)
