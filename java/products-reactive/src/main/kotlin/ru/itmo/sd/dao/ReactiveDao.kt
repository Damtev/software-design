package ru.itmo.sd.dao

import com.mongodb.client.model.Filters
import com.mongodb.rx.client.MongoDatabase
import org.bson.Document
import ru.itmo.sd.converter.CurrencyConverter
import ru.itmo.sd.model.Currency
import ru.itmo.sd.model.ListedProduct
import ru.itmo.sd.model.Product
import ru.itmo.sd.model.User
import rx.Observable
import rx.schedulers.Schedulers

class ReactiveDao(private val db: MongoDatabase, private val converter: CurrencyConverter) {
    fun getUserById(id: Long): Observable<User> = db.getCollection("users")
        .find(Filters.eq("id", id))
        .toObservable()
        .map {
            val currencyString = it.getString("currency")
            val currency = Currency.fromString(currencyString)
            User(id, currency)
        }.subscribeOn(Schedulers.io())

    fun getProductById(id: Long): Observable<Product> = db.getCollection("products")
        .find(Filters.eq("id", id))
        .toObservable()
        .map {
            val name = it.getString("name")
            val price = it.getDouble("price")
            val currencyString = it.getString("currency")
            val currency = Currency.fromString(currencyString)
            Product(
                id,
                name,
                price,
                currency
            )
        }.subscribeOn(Schedulers.io())

    fun addUser(user: User): Observable<Boolean> = getUserById(user.id).singleOrDefault(null).flatMap {
        if (it != null) {
            Observable.just(false)
        } else {
            db.getCollection("users")
                .insertOne(
                    Document(
                        mapOf(
                            "id" to user.id,
                            "currency" to user.currency.name
                        )
                    )
                )
                .asObservable()
                .isEmpty
                .map { emptyResult -> !emptyResult }
        }
    }

    fun addProduct(product: Product): Observable<Boolean> = getProductById(product.id).singleOrDefault(null).flatMap {
        if (it != null) {
            Observable.just(false)
        } else {
            db.getCollection("products")
                .insertOne(
                    Document(
                        mapOf(
                            "id" to product.id,
                            "name" to product.name,
                            "price" to product.price,
                            "currency" to product.currency.name
                        )
                    )
                )
                .asObservable()
                .isEmpty
                .map { emptyResult -> !emptyResult }
        }
    }

    fun getProducts(userId: Long): Observable<ListedProduct> = getUserById(userId).flatMap { user ->
        db.getCollection("products")
            .find()
            .toObservable()
            .map {
                val productId = it.getLong("id")
                val name = it.getString("name")
                val currency = Currency.fromString(it.getString("currency"))
                val price = it.getDouble("price")
                ListedProduct(productId, name, converter.convert(from = currency, to = user.currency, price = price))
            }.subscribeOn(Schedulers.io())
    }
}