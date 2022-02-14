package com.rbrauwers.exchange.service.data.remote.response

import com.squareup.moshi.Json

internal data class HistoricalExchangeRateResponse(
    val success: Boolean,

    @Json(name = "rate")
    val baseCurrency: String?,

    val rates: Map<String, Double>?
) {

    fun convertValue(fromCurrency: String, toCurrency: String): Double? {
        rates ?: return null

        return if (fromCurrency == baseCurrency) {
            rates[toCurrency]
        } else {
            val fromCurrencyNotAdjusted = rates[fromCurrency] ?: return null
            val toCurrencyNotAdjusted = rates[toCurrency] ?: return null
            return (1/fromCurrencyNotAdjusted) * toCurrencyNotAdjusted
        }
    }

}