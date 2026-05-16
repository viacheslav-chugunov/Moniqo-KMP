package io.github.viacheslav.chugunov.moniqo.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CurrencyRatesDto(
    @SerialName("date")
    val date: String,
    @SerialName("eur")
    val eur: Map<String, Double>,
)
