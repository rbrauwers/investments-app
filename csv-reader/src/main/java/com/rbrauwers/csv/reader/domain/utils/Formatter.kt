package com.rbrauwers.csv.reader.domain.utils

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

object Formatter {

    private val localePtBr = Locale("pt", "BR")

    private val usdDecimalFormatSymbols = DecimalFormatSymbols.getInstance().apply {
        currencySymbol = Currency.getInstance("USD").symbol
        decimalSeparator = '.'
    }

    private val brlDecimalFormatSymbols = DecimalFormatSymbols.getInstance().apply {
        currencySymbol = DecimalFormatSymbols(localePtBr).currencySymbol
    }

    private val brlDecimalFormat = DecimalFormat("¤###,###,##0.00", brlDecimalFormatSymbols)
    private val usdDecimalFormat = DecimalFormat("¤###,###,##0.00", usdDecimalFormatSymbols)

    fun formatBRL(value: Double?): String? {
        value ?: return null
        return brlDecimalFormat.format(value)
    }

    fun formatUSD(value: Double?): String? {
        value ?: return null
        return usdDecimalFormat.format(value)
    }

}