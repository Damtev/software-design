package ru.itmo.sd.command

import ru.itmo.sd.dao.ReactiveDao
import ru.itmo.sd.model.Product
import rx.Observable

class AddProductCommand(private val product: Product) : Command {
    override fun process(dao: ReactiveDao): Observable<String> = dao.addProduct(product).map { it.toString() }
}
