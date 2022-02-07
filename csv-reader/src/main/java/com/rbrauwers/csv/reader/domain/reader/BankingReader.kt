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
        val rawDate = "${words[Columns.DATE.index]}-${words[Columns.HOUR.index]}"

        return Transaction(
            rawDate = rawDate,
            valueBrl = null,
            valueUsd = CSVReader.parseDouble(words[Columns.VALUE_USD.index]),
            category = Category.fromRawTransaction(line),
            product = Product.fromRawTransaction(line),
            tax = null,
            exchangeRate = null,
            inputDateFormat = "MM/dd/yyyy-hh:mm"
        )
    }

    internal enum class Columns(val index: Int, val description: String) {
        DATE(0, "Date"),
        HOUR(1, "Hour"),
        VALUE_USD(4, "Value USD")
    }

}