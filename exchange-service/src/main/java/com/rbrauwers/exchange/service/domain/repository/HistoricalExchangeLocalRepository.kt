package com.rbrauwers.exchange.service.domain.repository

import com.rbrauwers.exchange.service.domain.model.ExchangeRate

/**
 * Repository to get historical exchange from local disk cache.
 */
internal interface HistoricalExchangeLocalRepository {

    suspend fun getHistoricalExchange(date: String): ExchangeRate?

    suspend fun saveHistoricalExchange(date: String, rate: ExchangeRate?)

}