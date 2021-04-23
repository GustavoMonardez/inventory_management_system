package inventory_management_system;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


/**
 *
 * The Inventory class simulates an inventory management system.
 * @author Gustavo Monardez
 */
public class Inventory {
    private static ObservableList<Part>allParts = FXCollections.observableArrayList();
    private static ObservableList<Product>allProducts = FXCollections.observableArrayList();

    /**
     * The addPart method adds a new part to
     * the allParts list
     * @param part The new part to add to the list
    */
    public static void addPart(Part part) {
        allParts.add(part);
    }

    /**
     * The addProduct method adds a new product to
     * the allProducts list
     * @param product The new product to add to the list
     */
    public static void addProduct(Product product) {
        allProducts.add(product);
    }

    /**
     * The lookupPart method looks up a part in the list
     * @param partId The id of the part to look for
     * @return The part being looked up or null if not found
     */
    public static Part lookupPart(int partId) {
        for (Part curr: allParts) {
            if (curr.getId() == partId) return curr;
        }
        return null;
    }

    /**
     * The lookupProduct method looks up a product in the list
     * @param productId The id of the product to look for
     * @return The product being looked up or null if not found
     */
    public static Product lookupProduct(int productId) {
        for (Product curr: allProducts) {
            if (curr.getId() == productId) return curr;
        }
        return null;
    }

    /**
     * The lookupPart method looks up a part in the list
     * @param partName The name of the part to look for
     * @return The part(s) being looked up or null if not found
     */
    public static ObservableList<Part> lookupPart(String partName) {
        ObservableList<Part> result = FXCollections.observableArrayList();
        for (Part part : allParts) {
            if (part.getName().toLowerCase().startsWith(partName)) {
                result.add(part);
            }
        }
        return result;
    }

    /**
     * The lookupProduct method looks up a product(s) in the list
     * @param productName The name of the product to look for
     * @return The product(s) being looked up or null if not found
     */
    public static ObservableList<Product> lookupProduct(String productName) {
        ObservableList<Product> result = FXCollections.observableArrayList();
        for (Product product : allProducts) {
            if (product.getName().toLowerCase().startsWith(productName)) {
                result.add(product);
            }
        }
        return result;
    }

    /**
     * The updatePart method, updates an existing part
     * @param index The index of the part to update
     * @param modifiedPart The updated information about the part
     */
    public static void updatePart(int index, Part modifiedPart) {
        // preserve the part id
        Part oldPart = allParts.get(index);
        modifiedPart.setId(oldPart.getId());

        // update all other fields
        allParts.set(index, modifiedPart);
    }

    /**
     * The updateProduct method, updates an existing part
     * @param index The index of the product to update
     * @param selectedProduct The updated information about the product
     */
    public static void updateProduct(int index, Product selectedProduct) {
        // preserve the product id
        Product oldProduct = allProducts.get(index);
        selectedProduct.setId(oldProduct.getId());

        // update all other fields
        allProducts.set(index, selectedProduct);
    }

    /**
     * The deletePart method, deletes an existing part
     * @param selectedPart The part to delete
     * @return true if the part is deleted successfully, false otherwise
     */
    public static boolean deletePart(Part selectedPart) {
        if (allParts.remove(selectedPart)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * The deleteProduct method, deletes an existing product
     * @param selectedProduct The product to delete
     * @return true if the product is deleted successfully, false otherwise
     */
    public static boolean deleteProduct(Product selectedProduct) {
        if (allProducts.remove(selectedProduct)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * The getAllParts method, returns all existing parts
     * @return List of all parts
     */
    public static ObservableList<Part> getAllParts() {
        return allParts;
    }

    /**
     * The getAllProducts method, returns all existing products
     * @return List of all products
     */
    public static ObservableList<Product> getAllProducts() {
        return allProducts;
    }
}
