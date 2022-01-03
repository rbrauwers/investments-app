package com.rbrauwers.investments.domain.model

import com.rbrauwers.csv.reader.domain.model.Product
import com.rbrauwers.csv.reader.domain.model.Transaction

internal data class TransactionsGroup(
    val date: String,
    val product: Product
) {

    private val _transactions = mutableListOf<Transaction>()
    val transactions: List<Transaction> = _transactions

    fun addTransaction(transaction: Transaction) {
        _transactions.add(transaction)
    }

}