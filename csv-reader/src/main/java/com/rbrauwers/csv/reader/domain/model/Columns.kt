package com.rbrauwers.csv.reader.domain.model

internal enum class Columns(val index: Int, val description: String) {
    DATE(0, "Date"),
    HOUR(1, "Hour"),
    VALUE(4, "Value")
}