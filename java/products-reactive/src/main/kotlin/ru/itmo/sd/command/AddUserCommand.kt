package ru.itmo.sd.command

import ru.itmo.sd.dao.ReactiveDao
import ru.itmo.sd.model.User
import rx.Observable

class AddUserCommand(private val user: User) : Command {
    override fun process(dao: ReactiveDao): Observable<String> = dao.addUser(user).map { it.toString() }

}
