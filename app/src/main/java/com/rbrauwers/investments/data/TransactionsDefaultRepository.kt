package com.rbrauwers.investments.data

import com.rbrauwers.csv.reader.CSVReader
import com.rbrauwers.csv.reader.domain.model.Transaction
import com.rbrauwers.investments.domain.repository.TransactionsRepository
import java.io.InputStream
import javax.inject.Inject

internal class TransactionsDefaultRepository @Inject constructor(private val reader: CSVReader) :
    TransactionsRepository {

    private var transactions: List<Transaction>? = null

    override suspend fun getTransactions(): List<Transaction> {
        if (transactions == null) {
            transactions = reader
                .parse(CSVReader.ReadOptions(skipFirstLine = true))
        }

        return transactions.orEmpty()
    }

}