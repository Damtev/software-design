package servlet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.akirakozov.sd.refactoring.servlet.QueryServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static utils.TestUtils.*;

public class QueryServletTest {

    private final HttpServletRequest request = mock(HttpServletRequest.class);
    private final HttpServletResponse response = mock(HttpServletResponse.class);
    private final QueryServlet queryServlet = new QueryServlet();
    private final Writer writer = new StringWriter();

    @BeforeEach
    public void setup() throws SQLException, IOException {
        commonTestSetup(response, writer);
    }

    private void commonQueryTest(final String command, final String answer) throws SQLException {
        commonQueryTest(command, answer, COMMON_INSERT_QUERY);
    }

    private void commonQueryTest(final String command, final String answer, final String insertQuery) throws SQLException {
        try (final Connection connection = DriverManager.getConnection(DB_ADDRESS)) {
            if (insertQuery != null && !insertQuery.isEmpty()) {
                connection.prepareStatement(insertQuery).execute();
            }
        }
        when(request.getParameter("command")).thenReturn(command);
        queryServlet.doGet(request, response);
        assertEquals(writer.toString(), answer);
    }

    @Test
    public void testCorrectMax() throws SQLException {
        commonQueryTest("max",
                """
                        <html><body>
                        <h1>Product with max price: </h1>
                        EXPENSIVE	31</br>
                        </body></html>
                        """);
    }

    @Test
    public void testCorrectMin() throws SQLException {
        commonQueryTest("min",
                """
                        <html><body>
                        <h1>Product with min price: </h1>
                        CHEAP	10</br>
                        </body></html>
                        """);
    }

    @Test
    public void testCorrectSum() throws SQLException {
        commonQueryTest("sum",
                """
                        <html><body>
                        Summary price:\s
                        66
                        </body></html>
                        """);
    }

    @Test
    public void testEmptySum() throws SQLException {
        commonQueryTest("sum",
                """
                        <html><body>
                        Summary price:\s
                        0
                        </body></html>
                        """,
                "");
    }

    @Test
    public void testCorrectCount() throws SQLException {
        commonQueryTest("count",
                """
                        <html><body>
                        Number of products:\s
                        3
                        </body></html>
                        """);
    }

    @Test
    public void testEmptyCount() throws SQLException {
        commonQueryTest("count",
                """
                        <html><body>
                        Number of products:\s
                        0
                        </body></html>
                        """,
                "");
    }

    @Test
    public void testUnknownCommand() throws SQLException {
        commonQueryTest("",
                """
                        Unknown command:\s
                        """);
    }
}
