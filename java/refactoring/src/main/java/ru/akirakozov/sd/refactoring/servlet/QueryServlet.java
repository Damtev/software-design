package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.html.HtmlResponseBuilder;
import ru.akirakozov.sd.refactoring.service.ProductService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @author akirakozov
 */
public class QueryServlet extends HttpServlet {

    private final ProductService productService = new ProductService();
    private final Map<String, Consumer<HtmlResponseBuilder>> commands = new HashMap<>() {{
        put("max", QueryServlet.this::max);
        put("min", QueryServlet.this::min);
        put("sum", QueryServlet.this::sum);
        put("count", QueryServlet.this::count);
    }};

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        final String command = request.getParameter("command");
        final HtmlResponseBuilder responseBuilder = new HtmlResponseBuilder();

        try {
            if (commands.containsKey(command)) {
                commands.get(command).accept(responseBuilder);
                response.getWriter().println(responseBuilder.toString());
            } else {
                response.getWriter().println("Unknown command: " + command);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }

    private void max(final HtmlResponseBuilder responseBuilder) {
        responseBuilder.appendHeader("Product with max price: ", 1);
        productService.getProductWithMaxPrice().ifPresent(product -> responseBuilder.append(product.toString()).appendBreakLine());
    }

    private void min(final HtmlResponseBuilder responseBuilder) {
        responseBuilder.appendHeader("Product with min price: ", 1);
        productService.getProductWithMinPrice().ifPresent(product -> responseBuilder.append(product.toString()).appendBreakLine());
    }

    private void sum(final HtmlResponseBuilder responseBuilder) {
        responseBuilder.appendNextLine("Summary price: ").appendNextLine(String.valueOf(productService.getSumPrice()));
    }

    private void count(final HtmlResponseBuilder responseBuilder) {
        responseBuilder.appendNextLine("Number of products: ").appendNextLine(String.valueOf(productService.getCountProducts()));
    }
}
