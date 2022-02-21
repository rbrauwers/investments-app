package com.rbrauwers.exchange.service.data.repository

import com.rbrauwers.exchange.service.domain.model.ExchangeRate
import com.rbrauwers.exchange.service.domain.repository.HistoricalExchangeLocalRepository
import com.rbrauwers.exchange.service.domain.repository.HistoricalExchangeRemoteRepository
import com.rbrauwers.exchange.service.domain.repository.HistoricalExchangeRepository
import javax.inject.Inject

internal class HistoricalExchangeRepositoryImpl @Inject constructor(
    private val localRepository: HistoricalExchangeLocalRepository,
    private val remoteRepository: HistoricalExchangeRemoteRepository
): HistoricalExchangeRepository {

    override suspend fun getHistoricalExchange(date: String): ExchangeRate? {
        val cachedExchangeRate = localRepository.getHistoricalExchange(date)
        if (cachedExchangeRate != null) {
            return cachedExchangeRate
        }

        return runCatching {
            remoteRepository.getHistoricalExchange(date)
        }.fold(
            onSuccess = { response ->
                ExchangeRate.fromResponse(response).also {
                    localRepository.saveHistoricalExchange(date, it)
                }
            },
            onFailure = {
                it.printStackTrace()
                null
            }
        )
    }

}