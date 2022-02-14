package com.rbrauwers.exchange.service.domain.repository

import com.rbrauwers.exchange.service.domain.model.ExchangeRate

interface HistoricalExchangeRepository {

    suspend fun getHistoricalExchange(date: String): ExchangeRate?

}