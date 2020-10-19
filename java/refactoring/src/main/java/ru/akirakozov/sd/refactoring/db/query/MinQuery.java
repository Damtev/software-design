package ru.akirakozov.sd.refactoring.db.query;

public class MinQuery extends ProductQuery {
    @Override
    public String toString() {
        return "SELECT * FROM PRODUCT ORDER BY PRICE LIMIT 1";
    }
}
