package ru.itmo.sd

import com.beust.klaxon.Klaxon
import com.mongodb.rx.client.MongoClients
import io.reactivex.netty.protocol.http.server.HttpServer
import ru.itmo.sd.command.Command
import ru.itmo.sd.config.AppConfig
import ru.itmo.sd.converter.CurrencyConverter
import ru.itmo.sd.dao.ReactiveDao
import java.io.File

const val CONFIG_PATH = "src/main/resources/config.json"

fun main() {
    val configuration = Klaxon().parse<AppConfig>(File(CONFIG_PATH))!!
    val database = MongoClients.create(configuration.db.toString()).getDatabase(configuration.db.name)
    val dao = ReactiveDao(
        database,
        CurrencyConverter(configuration.rate.toRate())
    )
    HttpServer.newServer(configuration.server.port).start { request, response ->
        response.writeString(
            Command.processRequest(request).process(dao).map { "$it\n" }
        )
    }.awaitShutdown()
}