package io.github.viacheslav.chugunov.moniqo.core.model

data class RatePair(
    val fromRate: Rate,
    val toRate: Rate,
    val baseCurrency: String,
    val updatedAt: String,
)
