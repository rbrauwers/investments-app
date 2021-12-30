package com.rbrauwers.investments.domain.repository

import com.rbrauwers.csv.reader.domain.model.Transaction
import java.io.InputStream

interface TransactionsRepository {

    suspend fun getTransactions(): List<Transaction>

}