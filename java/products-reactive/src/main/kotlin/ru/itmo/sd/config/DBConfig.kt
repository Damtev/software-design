package ru.itmo.sd.config

data class DBConfig(val schema: String, val host: String, val port: Int, val name: String) {
    override fun toString(): String = "$schema://$host:$port"
}