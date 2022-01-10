package com.rbrauwers.investments.domain.repository

import com.rbrauwers.csv.reader.domain.model.Transaction
import com.rbrauwers.investments.domain.model.TransactionsGroup
import java.io.InputStream

internal interface TransactionsRepository {

    suspend fun getStatementTransactions(): List<Transaction>

    suspend fun getStatementTransactionsGroups(): Set<TransactionsGroup>

    suspend fun getExchangeTransactions(): List<Transaction>

    suspend fun getExchangeTransactionsGroups(): Set<TransactionsGroup>

}