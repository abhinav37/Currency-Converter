package com.ajenterprise.paypaybaymax.home.network

data class CurrencyResponse(
    val success: Boolean = false,
    val terms: String = "",
    val privacy: String = "",
    val timestamp: Long = 1430401802,
    val source: String = "",
    val quotes: Map<String, Float>?
) {
    data class CurrencyData(
        val name: String = "",
        val rate: Float = 0f
    )
}