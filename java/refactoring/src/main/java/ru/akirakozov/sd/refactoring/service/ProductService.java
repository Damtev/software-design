package ru.akirakozov.sd.refactoring.service;

import ru.akirakozov.sd.refactoring.db.Product;
import ru.akirakozov.sd.refactoring.db.operations.query.MaxQuery;
import ru.akirakozov.sd.refactoring.db.operations.query.MinQuery;
import ru.akirakozov.sd.refactoring.db.operations.query.SumQuery;
import ru.akirakozov.sd.refactoring.db.repository.ProductRepository;

import java.util.List;
import java.util.Optional;

public class ProductService {

    private final ProductRepository productRepository = new ProductRepository();

    public void addProduct(final Product product) {
        try {
            productRepository.addProduct(product);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<Product> getProductWithMaxPrice() {
        try {
            return productRepository.executeProductQuery(new MaxQuery());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<Product> getProductWithMinPrice() {
        try {
            return productRepository.executeProductQuery(new MinQuery());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<Product> getAllProducts() {
        try {
            return productRepository.findAllProducts();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public int getSumPrice() {
        try {
            return productRepository.executeProductQuery(new SumQuery());
//            return getAllProducts().stream().map(Product::getPrice).reduce(0, Integer::sum);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public int getCountProducts() {
        try {
            return getAllProducts().size();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
