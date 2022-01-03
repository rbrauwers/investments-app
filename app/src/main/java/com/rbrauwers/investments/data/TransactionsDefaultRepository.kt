package com.rbrauwers.investments.data

import com.rbrauwers.csv.reader.CSVReader
import com.rbrauwers.csv.reader.domain.model.Transaction
import com.rbrauwers.investments.domain.model.TransactionsGroup
import com.rbrauwers.investments.domain.repository.TransactionsRepository
import javax.inject.Inject

internal class TransactionsDefaultRepository @Inject constructor(private val reader: CSVReader) :
    TransactionsRepository {

    private var transactions: List<Transaction>? = null
    private var transactionsGroups: Set<TransactionsGroup>? = null

    override suspend fun getTransactions(): List<Transaction> {
        if (transactions == null) {
            transactions = reader
                .parse(CSVReader.ReadOptions(skipFirstLine = true))
        }

        return transactions.orEmpty()
    }

    override suspend fun getTransactionsGroups(): Set<TransactionsGroup> {
        if (transactionsGroups == null) {
            val transactions = getTransactions()
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

            this.transactionsGroups = transactionsGroups
        }

        return transactionsGroups.orEmpty()
    }

}