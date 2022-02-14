package com.rbrauwers.exchange.service.data.repository

import com.rbrauwers.exchange.service.domain.model.ExchangeRate
import com.rbrauwers.exchange.service.domain.repository.HistoricalExchangeRemoteRepository
import com.rbrauwers.exchange.service.domain.repository.HistoricalExchangeRepository
import javax.inject.Inject

internal class HistoricalExchangeRepositoryImpl @Inject constructor(
    private val remoteRepository: HistoricalExchangeRemoteRepository
): HistoricalExchangeRepository {

    override suspend fun getHistoricalExchange(date: String): ExchangeRate? {
        return runCatching {
            remoteRepository.getHistoricalExchange(date)
        }.fold(
            onSuccess = { response ->
                ExchangeRate.fromResponse(response)
            },
            onFailure = {
                it.printStackTrace()
                null
            }
        )
    }

}