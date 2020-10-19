package ru.akirakozov.sd.refactoring.db.query;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class SumQuery implements Query<Optional<Integer>> {

    @Override
    public Optional<Integer> getResult(ResultSet resultSet) throws SQLException {
        return Optional.ofNullable(resultSet.next() ? resultSet.getInt(1) : null);
    }

    @Override
    public String toString() {
        return "SELECT SUM(price) FROM PRODUCT";
    }
}
