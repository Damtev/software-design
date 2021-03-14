package ru.itmo.sd.command

import ru.itmo.sd.dao.ReactiveDao
import rx.Observable

class GetUserCommand(private val id: Long) : Command {
    override fun process(dao: ReactiveDao): Observable<String> = dao.getUserById(id).map { it.toString() }
}
