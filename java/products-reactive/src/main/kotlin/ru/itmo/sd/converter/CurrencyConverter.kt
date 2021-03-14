package ru.itmo.sd.converter

import ru.itmo.sd.model.Currency

class CurrencyConverter(private val rate: Map<Currency, Map<Currency, Double>>) {
    fun convert(from: Currency, to: Currency, price: Double): Double = rate[from]?.get(to)?.times(price)
        ?: error("Converting from $from to $to is not supported")
}