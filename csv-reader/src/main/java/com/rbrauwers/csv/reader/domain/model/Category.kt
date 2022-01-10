package com.rbrauwers.csv.reader.domain.model

enum class Category(
    private val alternativeNames: Set<String> = emptySet(),
    val isAggregator: Boolean
) {

    BROKERAGE(isAggregator = true),
    BUY(alternativeNames = setOf("Compra"), isAggregator = true),
    DEPOSIT(alternativeNames = setOf("Ted recebido"), isAggregator = false),
    EXCHANGE(setOf("Câmbio", "Remessa"), isAggregator = false),
    EXCHANGE_TAX(setOf("IOF"), isAggregator = true),
    DIVIDEND(isAggregator = false),
    DIVIDEND_TAX(setOf("Dividend tax"), isAggregator = false),
    SELL(setOf("Venda"), isAggregator = true),
    UNKNOWN(isAggregator = false);

    companion object {
        fun fromRawTransaction(raw: String): Category {
            values().forEach { category ->
                if (raw.contains(category.name, ignoreCase = true)) {
                    return category
                }

                if (category.alternativeNames.any { alternativeName ->
                        raw.contains(alternativeName, ignoreCase = true)
                    }) {
                    return category
                }
            }

            return UNKNOWN
        }
    }

}