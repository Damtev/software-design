package servlet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.akirakozov.sd.refactoring.servlet.AddProductServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static utils.TestUtils.commonTestSetup;

public class AddProductServletTest {

    private final HttpServletRequest request = mock(HttpServletRequest.class);
    private final HttpServletResponse response = mock(HttpServletResponse.class);
    private final AddProductServlet addProductServlet = new AddProductServlet();
    private final Writer writer = new StringWriter();

    @BeforeEach
    public void setup() throws SQLException, IOException {
        commonTestSetup(response, writer);
    }

    private void correctAddTest() throws IOException {
        addProductServlet.doGet(request, response);
        assertEquals(writer.toString(), "OK" + System.lineSeparator());
    }

    @Test
    public void testCorrectAddOne() throws IOException {
        when(request.getParameter("name")).thenReturn("product");
        when(request.getParameter("price")).thenReturn("228");
        correctAddTest();
    }

    @Test
    public void testCorrectNoName() throws IOException {
        when(request.getParameter("price")).thenReturn("228");
        correctAddTest();
    }

    @Test
    public void testNoPrice() {
        when(request.getParameter("name")).thenReturn("product");
        assertThrows(NumberFormatException.class, () -> addProductServlet.doGet(request, response));
    }

    @Test
    public void testPriceNotNumber() {
        when(request.getParameter("name")).thenReturn("product");
        when(request.getParameter("price")).thenReturn("not a number");
        assertThrows(NumberFormatException.class, () -> addProductServlet.doGet(request, response));
    }

    @Test
    public void testNoParameters() {
        assertThrows(NumberFormatException.class, () -> addProductServlet.doGet(request, response));
    }
}
