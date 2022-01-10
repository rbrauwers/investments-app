package com.rbrauwers.csv.reader.domain.model

import com.rbrauwers.csv.reader.domain.reader.CSVReader

data class Transaction(
    val category: Category,
    val date: String,
    val hour: String,
    val product: Product,
    val value: String)
{

    companion object {
        fun fromRawLine(line: String): Transaction {
            val words = line.split(CSVReader.SEPARATOR)
            return Transaction(
                date = words[Columns.DATE.index],
                hour = words[Columns.HOUR.index],
                value = words[Columns.VALUE.index],
                category = Category.fromRawTransaction(line),
                product = Product.fromRawTransaction(line)
            )
        }
    }

}