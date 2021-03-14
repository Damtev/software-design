package ru.itmo.sd.model

enum class Currency {
    RUB, USD, EUR;

    companion object {
        fun fromString(currencyString: String): Currency {
            val toUpperCase = currencyString.toUpperCase()
            return if (toUpperCase in values().map { it.name }) {
                valueOf(toUpperCase)
            } else {
                throw IllegalArgumentException("Currency $currencyString is not supported")
            }
        }
    }
}
