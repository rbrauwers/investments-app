package com.rbrauwers.investments.domain.model

import com.rbrauwers.csv.reader.domain.model.Product
import com.rbrauwers.csv.reader.domain.model.Transaction

internal data class TransactionsGroup(
    val date: String,
    val product: Product
) {

    private val _transactions = mutableListOf<Transaction>()
    val transactions: List<Transaction> = _transactions

    var total: Double = 0.0

    fun addTransaction(transaction: Transaction) {
        _transactions.add(transaction)
        calcTotal()
    }

    private fun calcTotal() {
        total = transactions.sumOf { it.value.toDoubleOrNull() ?: 0.0 }
    }

}