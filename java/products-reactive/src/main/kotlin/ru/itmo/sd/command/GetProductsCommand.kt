package ru.itmo.sd.command

import ru.itmo.sd.dao.ReactiveDao
import rx.Observable

class GetProductsCommand(private val userId: Long) : Command {
    override fun process(dao: ReactiveDao): Observable<String> = dao.getProducts(userId).map { it.toString() }
}
