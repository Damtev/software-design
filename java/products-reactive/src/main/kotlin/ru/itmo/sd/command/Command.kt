package ru.itmo.sd.command

import io.reactivex.netty.protocol.http.server.HttpServerRequest
import ru.itmo.sd.dao.ReactiveDao
import ru.itmo.sd.model.Currency
import ru.itmo.sd.model.Product
import ru.itmo.sd.model.User
import rx.Observable
import java.lang.RuntimeException

interface Command {

    fun process(dao: ReactiveDao): Observable<String>

    companion object {
        private fun <T> HttpServerRequest<T>.getQueryParam(parameter: String): String =
            queryParameters[parameter]?.first() ?: error("Parameter $parameter was expected but not found")

        fun <T> processRequest(request: HttpServerRequest<T>): Command {
            return try {
                when (val command = request.decodedPath.substring(1)) {
                    "add_user" -> {
                        val id = request.getQueryParam("id").toLong()
                        val currency = Currency.fromString(request.getQueryParam("currency"))
                        AddUserCommand(User(id, currency))
                    }
                    "get_user" -> {
                        val id = request.getQueryParam("id").toLong()
                        return GetUserCommand(id)
                    }
                    "add_product" -> {
                        val id = request.getQueryParam("id").toLong()
                        val currency = Currency.fromString(request.getQueryParam("currency"))
                        val price = request.getQueryParam("price").toDouble()
                        val name = request.getQueryParam("name")
                        AddProductCommand(Product(id, name, price, currency))
                    }
                    "get_product" -> {
                        val id = request.getQueryParam("id").toLong()
                        GetProductCommand(id)
                    }
                    "list" -> {
                        val id = request.getQueryParam("id").toLong()
                        GetProductsCommand(id)
                    }
                    else -> throw IllegalArgumentException("No such command $command")
                }
            } catch (e: Exception) {
                throw RuntimeException(e)
            }
        }
    }
}