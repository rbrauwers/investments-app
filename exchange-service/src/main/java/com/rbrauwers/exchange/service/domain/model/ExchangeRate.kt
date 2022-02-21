package com.rbrauwers.exchange.service.domain.model

import com.rbrauwers.exchange.service.data.remote.response.HistoricalExchangeRateResponse

/**
 * Assumes USD as base currency
 */
data class ExchangeRate(val valueBrl: Double?) {

    companion object {
        internal fun fromResponse(response: HistoricalExchangeRateResponse?): ExchangeRate? {
            response ?: return null
            return ExchangeRate(
                valueBrl = response.convertValue(
                    fromCurrency = "USD",
                    toCurrency = "BRL"
                )
            )
        }
    }

}