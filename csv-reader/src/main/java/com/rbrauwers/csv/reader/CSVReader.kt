package com.rbrauwers.csv.reader

import com.rbrauwers.csv.reader.domain.model.Columns
import com.rbrauwers.csv.reader.domain.model.Product
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
                    transactions.add(parseLine(line))
                }
            }
        }

        return transactions
    }

    private fun parseLine(line: String): Transaction {
        val words = line.split(SEPARATOR)
        return Transaction(
            date = words[Columns.DATE.index],
            hour = words[Columns.HOUR.index],
            product = Product.fromRawTransaction(line)
        )
    }

    data class ReadOptions(val skipFirstLine: Boolean)

    companion object {
        private const val SEPARATOR = ","
    }

}