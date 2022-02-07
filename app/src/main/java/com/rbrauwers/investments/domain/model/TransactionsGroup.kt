package com.rbrauwers.investments.domain.model

import com.rbrauwers.csv.reader.domain.model.Product
import com.rbrauwers.csv.reader.domain.model.Transaction
import java.util.*

internal data class TransactionsGroup(
    val rawDate: String,
    val product: Product
) {

    private val _transactions = mutableListOf<Transaction>()
    val transactions: List<Transaction> = _transactions

    var totalInUsd: Double = 0.0; private set

    fun addTransaction(transaction: Transaction) {
        _transactions.add(transaction)
        calcTotalInUsd()
    }

    fun getDate(): Date? {
        return transactions.firstOrNull()?.getDate()
    }

    fun formatDate() : String? {
        return transactions.firstOrNull()?.formatDate()
    }

    private fun calcTotalInUsd() {
        totalInUsd = transactions.sumOf { it.valueUsd ?: 0.0 }
    }

}