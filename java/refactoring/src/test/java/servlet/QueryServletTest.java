package servlet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.akirakozov.sd.refactoring.servlet.QueryServlet;
import utils.TestUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static utils.TestUtils.DB_ADDRESS;

public class QueryServletTest {

    private final HttpServletRequest request = mock(HttpServletRequest.class);
    private final HttpServletResponse response = mock(HttpServletResponse.class);
    private final QueryServlet queryServlet = new QueryServlet();
    private final Writer writer = new StringWriter();

    @BeforeEach
    public void setup() throws SQLException, IOException {
        when(response.getWriter()).thenReturn(new PrintWriter(writer));
        TestUtils.recreateProductTable();
    }

    private void commonQueryTest(final String command, final String answer) throws IOException, SQLException {
        final Map<String, Integer> values = Map.of(
                "CHEAP", 10,
                "EXPENSIVE", 31,
                "MIDDLE", 25
        );
        final String valuesString = values
                .entrySet()
                .stream()
                .map(product -> String.format("\t('%s', ", product.getKey()) + product.getValue() + ")")
                .collect(Collectors.joining("," + System.lineSeparator()));
        final String insertQuery = "INSERT INTO PRODUCT(NAME, PRICE) VALUES" + System.lineSeparator() + valuesString;
        commonQueryTest(command, answer, insertQuery);
    }

    private void commonQueryTest(final String command, final String answer, final String insertQuery) throws SQLException, IOException {
        try (final Connection connection = DriverManager.getConnection(DB_ADDRESS)) {
            connection.prepareStatement(insertQuery).execute();
        }
        when(request.getParameter("command")).thenReturn(command);
        queryServlet.doGet(request, response);
        assertEquals(answer, writer.toString());
    }

    @Test
    public void testCorrectMax() throws SQLException, IOException {
        commonQueryTest("max",
                """
                        <html><body>
                        <h1>Product with max price: </h1>
                        EXPENSIVE	31</br>
                        </body></html>
                        """);
    }

    @Test
    public void testCorrectMin() throws SQLException, IOException {
        commonQueryTest("min",
                """
                        <html><body>
                        <h1>Product with min price: </h1>
                        CHEAP	10</br>
                        </body></html>
                        """);
    }

    @Test
    public void testCorrectSum() throws SQLException, IOException {
        commonQueryTest("sum",
                """
                        <html><body>
                        Summary price:\s
                        66
                        </body></html>
                        """);
    }

    @Test
    public void testCorrectCount() throws SQLException, IOException {
        commonQueryTest("count",
                """
                        <html><body>
                        Number of products:\s
                        3
                        </body></html>
                        """);
    }

    @Test
    public void testUnknownCommand() throws SQLException, IOException {
        commonQueryTest("",
                """
                        Unknown command:\s
                        """);
    }
}
