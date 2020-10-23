package ru.akirakozov.sd.refactoring.db.operations.query;

import ru.akirakozov.sd.refactoring.db.operations.DatabaseOperation;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface Query<T> extends DatabaseOperation {
    T getResult(final ResultSet resultSet) throws SQLException;
}
