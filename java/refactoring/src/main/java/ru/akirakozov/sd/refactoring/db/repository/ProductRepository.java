package ru.akirakozov.sd.refactoring.db.repository;

import ru.akirakozov.sd.refactoring.db.Product;
import ru.akirakozov.sd.refactoring.db.operations.query.AllProductsQuery;
import ru.akirakozov.sd.refactoring.db.operations.query.Query;
import ru.akirakozov.sd.refactoring.db.operations.update.SaveUpdate;
import ru.akirakozov.sd.refactoring.db.operations.update.Update;

import java.sql.*;
import java.util.List;

public class ProductRepository {

    private static final String DB_ADDRESS = "jdbc:sqlite:test.db";

    public List<Product> findAllProducts() throws SQLException {
        return executeProductQuery(new AllProductsQuery());
    }

    public void addProduct(final Product product) throws SQLException {
        executeProductUpdate(new SaveUpdate(product));
    }

    public void executeProductUpdate(final Update update) throws SQLException {
        try (final Connection connection = DriverManager.getConnection(DB_ADDRESS)) {
            final Statement statement = connection.createStatement();
            final String s = update.toDBQuery();
            statement.executeUpdate(s);
            statement.close();
        }
    }

    public <T> T executeProductQuery(final Query<T> query) throws SQLException {
        try (final Connection connection = DriverManager.getConnection(DB_ADDRESS)) {
            final Statement statement = connection.createStatement();
            final String s = query.toDBQuery();
            final ResultSet resultSet = statement.executeQuery(s);
            final T result = query.getResult(resultSet);

            resultSet.close();
            statement.close();

            return result;
        }
    }
}
