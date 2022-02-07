package com.rbrauwers.csv.reader.domain.model

import com.rbrauwers.csv.reader.domain.utils.Formatter
import java.text.SimpleDateFormat
import java.util.*

data class Transaction(
    val category: Category,
    val rawDate: String,
    val product: Product,
    val valueBrl: Double?,
    val valueUsd: Double?,
    val tax: Double?,
    val exchangeRate: Double?,
    val inputDateFormat: String)
{

    fun getDate(): Date? {
        return SimpleDateFormat(inputDateFormat, Locale.US).parse(rawDate)
    }

    fun formatDate(): String? {
        val date = getDate() ?: return null
        return outputDateFormat.format(date)
    }

    fun getFormattedValue(): String? {
        return when(category) {
            Category.FOREX_TAX, Category.EXCHANGE_RATE -> {
                Formatter.formatBRL(valueBrl)
            }

            else -> {
                Formatter.formatUSD(valueUsd)
            }
        }
    }

    companion object {
        private val outputDateFormat = SimpleDateFormat("yyyy-dd-MM", Locale.US)
    }

}