package com.rbrauwers.csv.reader.domain.reader

import com.rbrauwers.csv.reader.domain.model.Transaction
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

class CSVReader(private val inputStream: InputStream) {

    fun parse(options: ReadOptions): List<Transaction> {
        val reader = BufferedReader(InputStreamReader(inputStream))
        val transactions = mutableListOf<Transaction>()

        reader.useLines { lines ->
            lines.forEachIndexed { index, line ->
                if (!options.skipFirstLine || index != 0) {
                    transactions.add(Transaction.fromRawLine(line.replace("\"", "")))
                }
            }
        }

        return transactions
    }

    data class ReadOptions(val skipFirstLine: Boolean)

    companion object {
        internal const val SEPARATOR = ","
    }

}