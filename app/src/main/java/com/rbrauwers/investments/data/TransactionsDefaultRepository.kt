package com.rbrauwers.investments.data

import com.rbrauwers.csv.reader.domain.model.Transaction
import com.rbrauwers.csv.reader.domain.reader.CSVReader
import com.rbrauwers.investments.di.ExchangeReader
import com.rbrauwers.investments.di.StatementReader
import com.rbrauwers.investments.domain.model.TransactionsGroup
import com.rbrauwers.investments.domain.repository.TransactionsRepository
import javax.inject.Inject

internal class TransactionsDefaultRepository @Inject constructor(
    @StatementReader private val statementReader: CSVReader,
    @ExchangeReader private val exchangeReader: CSVReader,
    private val forexReader: CSVReader
) : TransactionsRepository {

    private var statementTransactions: List<Transaction>? = null
    private var statementTransactionsGroups: Set<TransactionsGroup>? = null
    private var exchangeTransactions: List<Transaction>? = null
    private var exchangeTransactionsGroups: Set<TransactionsGroup>? = null
    private var forexTransactions: List<Transaction>? = null

    override suspend fun getStatementTransactions(): List<Transaction> {
        if (statementTransactions == null) {
            statementTransactions = parseTransactions(statementReader)
        }

        return statementTransactions.orEmpty()
    }

    override suspend fun getStatementTransactionsGroups(): Set<TransactionsGroup> {
        if (statementTransactionsGroups == null) {
            val transactions = getStatementTransactions()
            statementTransactionsGroups = parseTransactionsGroups(transactions)
        }

        return statementTransactionsGroups.orEmpty()
    }

    override suspend fun getExchangeTransactions(): List<Transaction> {
        if (exchangeTransactions == null) {
            exchangeTransactions = parseTransactions(exchangeReader)
        }

        return exchangeTransactions.orEmpty()
    }

    override suspend fun getExchangeTransactionsGroups(): Set<TransactionsGroup> {
        if (exchangeTransactionsGroups == null) {
            val transactions = getExchangeTransactions()
            exchangeTransactionsGroups = parseTransactionsGroups(transactions)
        }

        return exchangeTransactionsGroups.orEmpty()
    }

    override suspend fun getForexTransactions(): List<Transaction> {
        if (forexTransactions == null) {
            forexTransactions = parseTransactions(forexReader)
        }

        return forexTransactions.orEmpty()
    }

    private fun parseTransactions(reader: CSVReader): List<Transaction> {
        return reader.parse(CSVReader.ReadOptions(skipFirstLine = true))
    }

    private fun parseTransactionsGroups(transactions: List<Transaction>): Set<TransactionsGroup> {
        val transactionsGroups = mutableSetOf<TransactionsGroup>()

        transactions.forEach { transaction ->
            if (!transaction.category.isAggregator) {
                return@forEach
            }

            val group = transactionsGroups.firstOrNull {
                it.date == transaction.date &&
                    it.product == transaction.product
            } ?: TransactionsGroup(
                date = transaction.date,
                product = transaction.product
            )

            group.addTransaction(transaction)
            transactionsGroups.add(group)
        }

        return transactionsGroups
    }

}