package ru.akirakozov.sd.refactoring.db.operations.query;

import ru.akirakozov.sd.refactoring.db.Product;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AllProductsQuery implements Query<List<Product>> {
    @Override
    public List<Product> getResult(ResultSet resultSet) throws SQLException {
        final List<Product> result = new ArrayList<>();
        while (resultSet.next()) {
            result.add(Product.fromResultSet(resultSet));
        }

        return result;
    }

    @Override
    public String toDBQuery() {
        return "SELECT * FROM PRODUCT";
    }
}
