package com.rbrauwers.investments.domain.repository

import com.rbrauwers.csv.reader.domain.model.Transaction
import com.rbrauwers.exchange.service.domain.model.ExchangeRate
import com.rbrauwers.investments.domain.model.TransactionsGroup
import java.io.InputStream
import java.util.*

internal interface TransactionsRepository {

    suspend fun getStatementTransactions(): List<Transaction>

    suspend fun getStatementTransactionsGroups(): Set<TransactionsGroup>

    suspend fun getExchangeTransactions(): List<Transaction>

    suspend fun getExchangeTransactionsGroups(): Set<TransactionsGroup>

    suspend fun getForexTransactions(): List<Transaction>

    suspend fun getTotalOfForexTaxes(untilDate: Date?): Double

    suspend fun getTotalOfForexValues(untilDate: Date?): Double

    suspend fun getHistoricalExchangeRate(date: String): ExchangeRate?

}