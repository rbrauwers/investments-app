package com.rbrauwers.investments.util

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

private val USD_SYMBOLS = DecimalFormatSymbols.getInstance().apply {
    currencySymbol = Currency.getInstance("USD").symbol
}

private val FORMAT_USD = DecimalFormat("¤ ###,###,##0.00", USD_SYMBOLS)

fun Double?.formatUSD(): String = FORMAT_USD.format(this ?: 0.0)