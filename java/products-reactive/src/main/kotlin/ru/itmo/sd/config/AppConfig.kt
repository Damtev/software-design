package ru.itmo.sd.config

data class AppConfig(val db: DBConfig, val server: ServerConfig, val rate: CurrencyConfig)
