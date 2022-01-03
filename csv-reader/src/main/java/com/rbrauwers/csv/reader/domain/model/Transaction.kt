package com.rbrauwers.csv.reader.domain.model

data class Transaction(
    val category: Category,
    val date: String,
    val hour: String,
    val product: Product,
    val value: String)