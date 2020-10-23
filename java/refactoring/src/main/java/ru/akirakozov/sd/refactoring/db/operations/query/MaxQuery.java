package ru.akirakozov.sd.refactoring.db.operations.query;

public class MaxQuery extends ProductQuery {
    @Override
    public String toDBQuery() {
        return "SELECT * FROM PRODUCT ORDER BY PRICE DESC LIMIT 1";
    }
}
