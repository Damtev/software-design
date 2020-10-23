package ru.akirakozov.sd.refactoring.db.operations.query;

public class MinQuery extends ProductQuery {
    @Override
    public String toDBQuery() {
        return "SELECT * FROM PRODUCT ORDER BY PRICE LIMIT 1";
    }
}
