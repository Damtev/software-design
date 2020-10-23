package ru.akirakozov.sd.refactoring.db;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Product {

    private final String name;
    private int price;

    public Product(String name, int price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return String.format("%s\t%d", name, price);
    }

    public static Product fromResultSet(final ResultSet resultSet) throws SQLException {
        String name = resultSet.getString("name");
        int price = resultSet.getInt("price");
        return new Product(name, price);
    }
}
