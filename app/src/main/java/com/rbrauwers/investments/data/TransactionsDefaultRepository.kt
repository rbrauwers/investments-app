package com.rbrauwers.investments.data

import com.rbrauwers.csv.reader.domain.model.Category
import com.rbrauwers.csv.reader.domain.model.Product
import com.rbrauwers.csv.reader.domain.model.Transaction
import com.rbrauwers.csv.reader.domain.reader.CSVReader
import com.rbrauwers.investments.di.ExchangeReader
import com.rbrauwers.investments.di.StatementReader
import com.rbrauwers.investments.domain.model.TransactionsGroup
import com.rbrauwers.investments.domain.repository.TransactionsRepository
import java.util.*
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

    override suspend fun getTotalOfForexTaxes(untilDate: Date?): Double {
        return filterForexTransactions(untilDate).sumOf { it.tax ?: 0.0 }
    }

    override suspend fun getTotalOfForexValues(untilDate: Date?): Double {
        return filterForexTransactions(untilDate).sumOf { it.valueUsd ?: 0.0 }
    }

    private fun parseTransactions(reader: CSVReader): List<Transaction> {
        return reader.parse(CSVReader.ReadOptions(skipFirstLine = true))
    }

    private suspend fun parseTransactionsGroups(transactions: List<Transaction>): Set<TransactionsGroup> {
        val transactionsGroups = mutableSetOf<TransactionsGroup>()

        transactions.forEach { transaction ->
            if (!transaction.category.isAggregator) {
                return@forEach
            }

            val group = transactionsGroups.firstOrNull {
                it.rawDate == transaction.rawDate &&
                        it.product == transaction.product
            } ?: TransactionsGroup(
                rawDate = transaction.rawDate,
                product = transaction.product
            )

            group.addTransaction(transaction)
            transactionsGroups.add(group)
        }

        addRelativeForexTaxesTransactions(transactionsGroups)
        addUsdExchangeRateTransactions(transactionsGroups)

        return transactionsGroups
    }

    private suspend fun filterForexTransactions(untilDate: Date?): List<Transaction> {
        return getForexTransactions().let {
            if (untilDate == null) {
                it
            } else {
                it.filter { transaction ->
                    val date = transaction.getDate() ?: return@filter false
                    return@filter date.time <= untilDate.time
                }
            }
        }
    }

    private suspend fun getClosestForexTransaction(
        date: Date?,
        useFirstForexAsPlaceholder: Boolean
    ): Transaction? {
        println("qqq getClosestForexTransaction date: ${date}")
        date ?: return null

        // Relaxes forex date by 10 days
        val windowDate = Calendar.getInstance().apply {
            time = date
            add(Calendar.DAY_OF_YEAR, -10)
        }.time

        val forexTransactions = getForexTransactions()
            .sortedByDescending { it.getDate()?.time }

        val index = forexTransactions
            .indexOfFirst {
                val forexTime = it.getDate()?.time ?: 0
                forexTime < date.time && forexTime > windowDate.time
            }

        return forexTransactions.getOrNull(index)
            ?: if (useFirstForexAsPlaceholder) forexTransactions.firstOrNull() else
                null
    }

    /**
     * Adds relative forex taxes for each transactions group.
     * Relative forex tax, given a group G with buy value B:
     *  - Sum all forex taxes until G's date (T)
     *  - Sum all forex values until G's date (V)
     *  - Relative tax will be (B/V)*T
     */
    private suspend fun addRelativeForexTaxesTransactions(groups: Set<TransactionsGroup>) {
        var sumOfRelativeValues = 0.0
        val sumOfForexTaxes = getForexTransactions().sumOf { it.tax ?: 0.0 }

        groups.forEach { group ->
            val totalOfForexValues = getTotalOfForexValues(untilDate = group.getDate())
            val totalOfForexTaxes = getTotalOfForexTaxes(untilDate = group.getDate())
            val buyValue = group.transactions.firstOrNull { it.category == Category.BUY }
                ?.valueUsd ?: 0.0
            val relativeValue = (buyValue / totalOfForexValues) * totalOfForexTaxes
            sumOfRelativeValues += relativeValue

            /*
            println("qqq date: ${group.getDate()}")
            println("qqq buyValue: $buyValue")
            println("qqq totalOfForexTaxes: $totalOfForexTaxes")
            println("qqq totalOfForexValues: $totalOfForexValues")
            println("qqq relativeValue: $relativeValue")
            */

            group.addTransaction(
                Transaction(
                    category = Category.FOREX_TAX,
                    rawDate = group.rawDate,
                    product = Product.FOREX,
                    valueBrl = relativeValue,
                    valueUsd = null,
                    tax = null,
                    exchangeRate = null,
                    inputDateFormat = group.transactions.firstOrNull()?.inputDateFormat
                        ?: "yyyy-dd-MM"
                )
            )
        }

        println("sumOfRelativeValues: $sumOfRelativeValues")
        println("sumOfForexTaxes: $sumOfForexTaxes")
    }

    private suspend fun addUsdExchangeRateTransactions(groups: Set<TransactionsGroup>) {
        groups.forEachIndexed { index, group ->
            val exchangeRate = getClosestForexTransaction(
                date = group.getDate(),
                useFirstForexAsPlaceholder = index == 0
            )?.exchangeRate

            println("qqq exchangeRate: $exchangeRate")

            group.addTransaction(
                Transaction(
                    category = Category.EXCHANGE_RATE,
                    rawDate = group.rawDate,
                    product = Product.FOREX,
                    valueBrl = exchangeRate,
                    valueUsd = 1.0,
                    tax = null,
                    exchangeRate = exchangeRate,
                    inputDateFormat = group.transactions.firstOrNull()?.inputDateFormat
                        ?: "yyyy-dd-MM"
                )
            )
        }
    }

}