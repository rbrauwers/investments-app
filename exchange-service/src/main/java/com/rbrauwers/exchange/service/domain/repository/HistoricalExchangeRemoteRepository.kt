package com.rbrauwers.exchange.service.domain.repository

import com.rbrauwers.exchange.service.data.remote.response.HistoricalExchangeRateResponse
import retrofit2.http.Query

internal interface HistoricalExchangeRemoteRepository {

    suspend fun getHistoricalExchange(@Query("") date: String): HistoricalExchangeRateResponse

}