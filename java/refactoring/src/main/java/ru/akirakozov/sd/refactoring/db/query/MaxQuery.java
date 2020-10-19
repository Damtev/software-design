package ru.akirakozov.sd.refactoring.db.query;

public class MaxQuery extends ProductQuery {
    @Override
    public String toString() {
        return "SELECT * FROM PRODUCT ORDER BY PRICE DESC LIMIT 1";
    }
}
