package com.rbrauwers.csv.reader.domain.reader

import com.rbrauwers.csv.reader.domain.model.Category
import com.rbrauwers.csv.reader.domain.model.Product
import com.rbrauwers.csv.reader.domain.model.Transaction
import com.rbrauwers.csv.reader.domain.utils.Formatter
import com.rbrauwers.csv.reader.domain.utils.splitCSVLine
import java.io.InputStream

class ForexReader(private val inputStream: InputStream) : CSVReader {

    override fun getInputStream(): InputStream {
        return inputStream
    }

    override fun parseLine(line: String): Transaction {
        val words = line.splitCSVLine(CSVReader.SEPARATOR)
        val rawDate = "${words[Columns.DATE.index]}-${words[Columns.HOUR.index]}"

        /*
        println("qqq valueBrl: ${words[Columns.VALUE_BRL.index]}")
        println("qqq valueBrl: ${CSVReader.parseDouble(words[Columns.VALUE_BRL.index])}")
        println("qqq valueUsd: ${words[Columns.VALUE_USD.index]}")
        println("qqq valueUsd: ${CSVReader.parseDouble(words[Columns.VALUE_USD.index])}")
        println("qqq tax: ${words[Columns.TAX.index]}")
        println("qqq tax: ${CSVReader.parseDouble(words[Columns.TAX.index])}")
        println("qqq [1] exchange rate fom ForexReader: ${words[Columns.EXCHANGE_RATE.index]}")
        println("qqq [2] exchange rate fom ForexReader: ${CSVReader.parseDouble(words[Columns.EXCHANGE_RATE.index])}")
        */

        return Transaction(
            rawDate = rawDate,
            valueBrl = CSVReader.parseDouble(words[Columns.VALUE_BRL.index]),
            valueUsd = CSVReader.parseDouble(words[Columns.VALUE_USD.index]),
            tax = CSVReader.parseDouble(words[Columns.TAX.index]),
            exchangeRate = CSVReader.parseDouble(words[Columns.EXCHANGE_RATE.index]),
            category = Category.EXCHANGE,
            product = Product.FOREX,
            inputDateFormat = "dd/MM/yyyy-hh:mm"
        )
    }

    internal enum class Columns(val index: Int, val description: String) {
        DATE(0, "Date"),
        HOUR(1, "Hour"),
        VALUE_BRL(4, "Value BRL"),
        VALUE_USD(5, "Value USD"),
        TAX(6, "Tax"),
        EXCHANGE_RATE(7, "Exchange rate")
    }

}