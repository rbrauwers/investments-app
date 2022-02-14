package configs.builder

import java.io.File
import java.io.FileInputStream
import java.util.*

fun testingDebug(name: String) {
    println("Testing debug: $name")
}

fun getExchangeRatesApiKey(configsFile: File): String {
    if (configsFile.exists()) {
        val properties = Properties()
        properties.load(FileInputStream(configsFile))
        return (properties["exchange.rates.api.key"] as? String).orEmpty()
    } else {
        throw IllegalArgumentException("Invalid exchange configs file.")
    }
}