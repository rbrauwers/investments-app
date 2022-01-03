package com.rbrauwers.csv.reader.domain.model

enum class Category(
    private val alternativeNames: Set<String> = emptySet(),
    val isAggregator: Boolean
) {

    BROKERAGE(isAggregator = true),
    BUY(alternativeNames = setOf("Compra"), isAggregator = true),
    EXCHANGE(setOf("Câmbio"), isAggregator = false),
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
                        raw.contains(alternativeName)
                    }) {
                    return category
                }
            }

            return Category.UNKNOWN
        }
    }

}