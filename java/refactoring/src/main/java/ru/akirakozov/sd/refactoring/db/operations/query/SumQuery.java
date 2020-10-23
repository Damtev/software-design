package ru.akirakozov.sd.refactoring.db.operations.query;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SumQuery implements Query<Integer> {

    @Override
    public Integer getResult(ResultSet resultSet) throws SQLException {
        return resultSet.getInt(1);
    }

    @Override
    public String toDBQuery() {
        return "SELECT SUM(price) FROM PRODUCT";
    }
}
