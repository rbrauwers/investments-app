package com.rbrauwers.csv.reader.domain.model

enum class Product(private val alternativeNames: Set<String> = emptySet()) {
    EFAV,
    EXCHANGE(setOf("Câmbio")),
    FNDC,
    IBB,
    SLYV,
    SPHQ,
    VSS,
    VWO,
    UNKNOWN;

    companion object {
        fun fromRawTransaction(raw: String): Product {
            values().forEach { product ->
                if (raw.contains(product.name, ignoreCase = true)) {
                    return product
                }

                if (product.alternativeNames.any { alternativeName ->
                        raw.contains(alternativeName)
                    }) {
                    return product
                }
            }

            return UNKNOWN
        }
    }
}