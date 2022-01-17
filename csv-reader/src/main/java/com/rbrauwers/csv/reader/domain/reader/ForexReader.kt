package com.rbrauwers.csv.reader.domain.reader

import com.rbrauwers.csv.reader.domain.model.Category
import com.rbrauwers.csv.reader.domain.model.Product
import com.rbrauwers.csv.reader.domain.model.Transaction
import com.rbrauwers.csv.reader.domain.utils.splitCSVLine
import java.io.InputStream

class ForexReader(private val inputStream: InputStream) : CSVReader {

    override fun getInputStream(): InputStream {
        return inputStream
    }

    override fun parseLine(line: String): Transaction {
        val words = line.splitCSVLine(CSVReader.SEPARATOR)
        return Transaction(
            date = words[Columns.DATE.index],
            hour = words[Columns.HOUR.index],
            valueBrl = words[Columns.VALUE_BRL.index],
            valueUsd = words[Columns.VALUE_USD.index],
            category = Category.EXCHANGE,
            product = Product.EXCHANGE
        ).apply {
            println("Words: $words")
            println("Value BRL: $valueBrl")
            println("Value USD: $valueUsd")
        }
    }

    internal enum class Columns(val index: Int, val description: String) {
        DATE(0, "Date"),
        HOUR(1, "Hour"),
        VALUE_BRL(4, "Value BRL"),
        VALUE_USD(5, "Value USD"),
        TAXES(6, "Taxes")
    }

}