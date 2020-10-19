package ru.akirakozov.sd.refactoring.db.query;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface Query<T> {

    public T getResult(final ResultSet resultSet) throws SQLException;
}
