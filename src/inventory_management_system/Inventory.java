package inventory_management_system;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class Inventory {
    private ObservableList<Part>allParts;
    private ObservableList<Product>allProducts;

    Inventory(){
        this.allParts = FXCollections.observableArrayList();
    };

    void addPart(Part part) {
        this.allParts.add(part);
    }

    void addProduct(Product product) {
        this.allProducts.add(product);
    }

    Part lookupPart(int partId) {
        for (Part curr: allParts) {
            if (curr.getId() == partId) return curr;
        }
        return null;
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

    void updatePart(int index, Part modifiedPart) {
        // preserve the part id
        Part oldPart = this.allParts.get(index);
        modifiedPart.setId(oldPart.getId());

        // update all other fields
        this.allParts.set(index, modifiedPart);
    }

    void updateProduct(int index, Product selectedProduct) {

    }

    boolean deletePart(Part selectedPart) {
        if (this.allParts.remove(selectedPart)) {
            return true;
        } else {
            return false;
        }
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
