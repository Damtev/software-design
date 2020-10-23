package ru.akirakozov.sd.refactoring.db.operations.query;

import ru.akirakozov.sd.refactoring.db.Product;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public abstract class ProductQuery implements Query<Optional<Product>> {

    @Override
    public Optional<Product> getResult(ResultSet resultSet) throws SQLException {
        return Optional.ofNullable(resultSet.next() ? Product.fromResultSet(resultSet) : null);
    }

    public abstract String toDBQuery();
}
