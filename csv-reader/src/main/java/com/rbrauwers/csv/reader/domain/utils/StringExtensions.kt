package com.rbrauwers.csv.reader.domain.utils

internal fun String.splitCSVLine(separator: String) : List<String> {
    val words = split(separator)
    val sanitizedWords = mutableListOf<String>()

    words.forEachIndexed { index, word ->
        if (word.startsWith("\"")) {
            sanitizedWords.add("$word,${words[index+1]}".replace("\"", ""))
        } else if (!word.endsWith("\"")) {
            sanitizedWords.add(word)
        }
    }

    return sanitizedWords
}