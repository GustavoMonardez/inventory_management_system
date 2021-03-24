package inventory_management_system;

import javafx.collections.ObservableList;

public class Inventory {
    private ObservableList<Part>allParts;
    private ObservableList<Product>allProducts;

    void addPart(Part part) {
        this.allParts.add(part);
    }

    void addProduct(Product product) {
        this.allProducts.add(product);
    }

    Part lookupPart(int partId) {
        return allParts.get(partId);
    }

    Product lookupProduct(int productId) {
        return allProducts.get(productId);
    }

    ObservableList<Part> lookupPart(String partName) {
        return allParts;
    }

    ObservableList<Product> lookupProduct(String productName) {
        return allProducts;
    }

    void updatePart(int index, Part selectedPart) {

    }

    void updateProduct(int index, Product selectedProduct) {

    }

    boolean deletePart(Part selectedPart) {
        return true;
    }

    boolean deleteProduct(Product selectedProduct) {
        return true;
    }

    ObservableList<Part> getAllParts() {
        return this.allParts;
    }

    ObservableList<Product> getAllProducts() {
        return this.allProducts;
    }
}
