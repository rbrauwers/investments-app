package com.rbrauwers.investments.domain.repository

import com.rbrauwers.csv.reader.domain.model.Transaction
import com.rbrauwers.investments.domain.model.TransactionsGroup
import java.io.InputStream

internal interface TransactionsRepository {

    suspend fun getTransactions(): List<Transaction>

    suspend fun getTransactionsGroups(): Set<TransactionsGroup>

}