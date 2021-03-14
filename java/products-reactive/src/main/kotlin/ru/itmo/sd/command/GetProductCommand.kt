package ru.itmo.sd.command

import ru.itmo.sd.dao.ReactiveDao
import rx.Observable

class GetProductCommand(private val id: Long) : Command {
    override fun process(dao: ReactiveDao): Observable<String> = dao.getProductById(id).map { it.toString() }
}
