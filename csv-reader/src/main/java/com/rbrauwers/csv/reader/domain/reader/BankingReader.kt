package com.rbrauwers.csv.reader.domain.reader

import com.rbrauwers.csv.reader.domain.model.Category
import com.rbrauwers.csv.reader.domain.model.Product
import com.rbrauwers.csv.reader.domain.model.Transaction
import com.rbrauwers.csv.reader.domain.utils.splitCSVLine
import java.io.InputStream

class BankingReader(private val inputStream: InputStream): CSVReader {

    override fun getInputStream(): InputStream {
        return inputStream
    }

    override fun parseLine(line: String): Transaction {
        val words = line.splitCSVLine(CSVReader.SEPARATOR)
        return Transaction(
            date = words[Columns.DATE.index],
            hour = words[Columns.HOUR.index],
            valueBrl = null,
            valueUsd = words[Columns.VALUE_USD.index],
            category = Category.fromRawTransaction(line),
            product = Product.fromRawTransaction(line)
        )
    }

    internal enum class Columns(val index: Int, val description: String) {
        DATE(0, "Date"),
        HOUR(1, "Hour"),
        VALUE_USD(4, "Value USD")
    }

}