package com.rbrauwers.exchange.service.data.remote.api

import com.rbrauwers.exchange.service.data.remote.response.HistoricalExchangeRateResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

internal interface HistoricalExchangeAPI {

    /**
     * Free plans supports only EUR as base currency.
     * Conversion using USD as base currency are made manually.
     */
    @GET("{date}")
    suspend fun getHistoricalExchange(
        @Path("date") date: String,
        @Query("access_key") accessKey: String,
        @Query("base") baseCurrency: String = "EUR",
        @Query("symbols") symbols: String = "USD,BRL"): HistoricalExchangeRateResponse

}

//https://exchangeratesapi.io/documentation/#historicalrates

/*
https://api.exchangeratesapi.io/v1/2013-12-24
? access_key = API_KEY
& base = GBP
& symbols = USD,CAD,EUR
 */