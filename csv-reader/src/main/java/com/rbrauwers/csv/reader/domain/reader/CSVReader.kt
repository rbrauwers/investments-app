package com.rbrauwers.csv.reader.domain.reader

import com.rbrauwers.csv.reader.domain.model.Transaction
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

interface CSVReader {

    fun getInputStream(): InputStream

    fun parseLine(line: String): Transaction

    fun parse(options: ReadOptions): List<Transaction> {
        val reader = BufferedReader(InputStreamReader(getInputStream()))
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

    data class ReadOptions(val skipFirstLine: Boolean)

    companion object {
        internal const val SEPARATOR = ","

        internal fun parseDouble(raw: String): Double? {
            val sb = StringBuilder(raw.replace("\"", "").trim())

            sb.filter {
                it.isDigit() || (it == ',') || (it == '-') || (it == '.')
            }

            val decimalSeparatorIndex = maxOf(sb.lastIndexOf('.'), sb.lastIndexOf(','))
            if (decimalSeparatorIndex != -1) {
                sb[decimalSeparatorIndex] = '.'
            }

            sb.forEachIndexed { index, c ->
                if (index < decimalSeparatorIndex && (c == '.' || c == ',')) {
                    sb.deleteCharAt(index)
                }
            }

            return sb.toString().toDoubleOrNull()
        }
    }

}
