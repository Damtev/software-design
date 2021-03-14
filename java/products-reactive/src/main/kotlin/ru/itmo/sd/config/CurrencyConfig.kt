package ru.itmo.sd.config

import ru.itmo.sd.model.Currency

data class CurrencyConfig(val usd: Double, val eur: Double) {

    fun toRate(): Map<Currency, Map<Currency, Double>> = hashMapOf(
        Currency.RUB to hashMapOf(
            Currency.RUB to 1.0,
            Currency.USD to 1.0 / usd,
            Currency.EUR to 1.0 / eur
        ),
        Currency.USD to hashMapOf(
            Currency.RUB to usd,
            Currency.USD to 1.0,
            Currency.EUR to eur / usd
        ),
        Currency.EUR to hashMapOf(
            Currency.RUB to eur,
            Currency.USD to usd / eur,
            Currency.EUR to 1.0
        )
    )
}
