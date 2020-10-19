package utils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;
import java.util.stream.Collectors;

import static org.mockito.Mockito.when;

public class TestUtils {

    public static final String DB_ADDRESS = "jdbc:sqlite:test.db";
    public static final Map<String, Integer> COMMON_VALUES = Map.of(
            "CHEAP", 10,
            "EXPENSIVE", 31,
            "MIDDLE", 25
    );
    public static final String COMMON_INSERT_QUERY = "INSERT INTO PRODUCT(NAME, PRICE) VALUES" + System.lineSeparator() + COMMON_VALUES.entrySet()
            .stream()
            .map(product -> String.format("\t('%s', ", product.getKey()) + product.getValue() + ")")
            .collect(Collectors.joining("," + System.lineSeparator()));

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

    public static void commonTestSetup(HttpServletResponse response, Writer writer) throws SQLException, IOException {
        when(response.getWriter()).thenReturn(new PrintWriter(writer));
        recreateProductTable();
    }
}
