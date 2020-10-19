package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TestUtils {

    public static final String DB_ADDRESS = "jdbc:sqlite:test.db";

    public static void recreateProductTable() throws SQLException {
        try (final Connection connection = DriverManager.getConnection(DB_ADDRESS)) {
            final String dropTableQuery = "DROP TABLE IF EXISTS PRODUCT";
            final String createTableQuery = """
                    
                    CREATE TABLE IF NOT EXISTS PRODUCT(
                        ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        NAME           TEXT    NOT NULL,
                        PRICE          INT     NOT NULL
                    )
                    """;
            connection.prepareStatement(dropTableQuery).execute();
            connection.prepareStatement(createTableQuery).execute();
        }
    }
}
