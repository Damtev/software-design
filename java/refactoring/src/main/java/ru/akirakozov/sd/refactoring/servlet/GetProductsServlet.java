package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.db.Product;
import ru.akirakozov.sd.refactoring.html.HtmlResponseBuilder;
import ru.akirakozov.sd.refactoring.service.ProductService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author akirakozov
 */
public class GetProductsServlet extends HttpServlet {

    private final ProductService productService = new ProductService();

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        final List<Product> result = productService.getAllProducts();
        final HtmlResponseBuilder responseBuilder = new HtmlResponseBuilder();

        try {
            for (final Product product : result) {
                responseBuilder.append(product.toString()).appendBreakLine();
            }
            response.getWriter().println(responseBuilder.toString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
