package servlet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.akirakozov.sd.refactoring.servlet.GetProductsServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static utils.TestUtils.*;

public class GetProductsServletTest {

    private final HttpServletRequest request = mock(HttpServletRequest.class);
    private final HttpServletResponse response = mock(HttpServletResponse.class);
    private final GetProductsServlet getProductsServlet = new GetProductsServlet();
    private final Writer writer = new StringWriter();

    @BeforeEach
    public void setup() throws SQLException, IOException {
        commonTestSetup(response, writer);
    }

    private void commonGetTest(final String answer) throws IOException, SQLException {
        commonGetTest(answer, COMMON_INSERT_QUERY);
    }

    private void commonGetTest(final String answer, final String insertQuery) throws SQLException, IOException {
        try (final Connection connection = DriverManager.getConnection(DB_ADDRESS)) {
            if (insertQuery != null && !insertQuery.isEmpty()) {
                connection.prepareStatement(insertQuery).execute();
            }
        }
        getProductsServlet.doGet(request, response);
        assertEquals(writer.toString().split(System.lineSeparator()).length, answer.split(System.lineSeparator()).length);
    }

    @Test
    public void testCorrectGetFromEmptyDB() throws SQLException, IOException {
        commonGetTest("""
                <html><body>
                </body></html>
                """, "");
    }

    @Test
    public void testCorrectGet() throws SQLException, IOException {
        final String expected = "<html><body>"
                + System.lineSeparator()
                + COMMON_VALUES
                .entrySet()
                .stream()
                .map(product -> String.format("%s\t%s</br>%s", product.getKey(), product.getValue(), System.lineSeparator()))
                .collect(Collectors.joining())
                + "</body></html>"
                + System.lineSeparator();
        commonGetTest(expected);
    }
}
