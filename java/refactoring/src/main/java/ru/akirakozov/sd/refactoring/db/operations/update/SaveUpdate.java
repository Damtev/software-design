package ru.akirakozov.sd.refactoring.db.operations.update;
import ru.akirakozov.sd.refactoring.db.Product;

public class SaveUpdate implements Update {

    private final Product product;

    public SaveUpdate(Product product) {
        this.product = product;
    }

    @Override
    public String toDBQuery() {
        return String.format("INSERT INTO PRODUCT (NAME, PRICE) VALUES (\"%s\", %d)", product.getName(), product.getPrice());
    }
}
