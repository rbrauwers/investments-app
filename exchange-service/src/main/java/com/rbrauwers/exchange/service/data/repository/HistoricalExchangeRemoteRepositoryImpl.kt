package com.rbrauwers.exchange.service.data.repository

import com.rbrauwers.exchange.service.BuildConfig
import com.rbrauwers.exchange.service.data.remote.api.HistoricalExchangeAPI
import com.rbrauwers.exchange.service.data.remote.response.HistoricalExchangeRateResponse
import com.rbrauwers.exchange.service.domain.repository.HistoricalExchangeRemoteRepository
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Inject

internal class HistoricalExchangeRemoteRepositoryImpl @Inject constructor() :
    HistoricalExchangeRemoteRepository {

    private val api: HistoricalExchangeAPI by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(
                OkHttpClient()
                    .newBuilder()
                    .addInterceptor(
                        HttpLoggingInterceptor()
                            .setLevel(HttpLoggingInterceptor.Level.BODY)
                    )
                    .build()
            )
            .addConverterFactory(MoshiConverterFactory.create(Moshi.Builder().build()))
            .build()

        retrofit.create(HistoricalExchangeAPI::class.java)
    }

    override suspend fun getHistoricalExchange(date: String): HistoricalExchangeRateResponse {
        return api.getHistoricalExchange(
            date = date,
            accessKey = BuildConfig.EXCHANGE_RATES_API_KEY)
    }

    companion object {
        const val BASE_URL = "http://api.exchangeratesapi.io/v1/"
    }

}