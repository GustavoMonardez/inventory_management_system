package inventory_management_system;

import javafx.collections.ObservableList;

/**
 The Product class simulates products made out of parts.
 * @author Gustavo Monardez
 */
public class Product {
    private ObservableList<Part> associatedParts;
    private int id;
    private String name;
    private double price;
    private int stock;
    private int min;
    private int max;

    /**
     * The constructor initializes the products properties
     * @param id The product's ID
     * @param name The product's name
     * @param price The product's price
     * @param stock Quantity in stock
     * @param min Minimum value
     * @param max Maximum value
     */
    public Product(int id, String name, double price, int stock, int min, int max) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
    }

    /**
     *  The getID method returns the product's id
     *  @return The value in the id field.
     */
    public int getId() {
        return this.id;
    }

    /**
     *  The getName method returns the product's name
     *  @return The value in the name field.
     */
    public String getName() {
        return this.name;
    }

    /**
     *  The getPrice method returns the product's price
     *  @return The value in the price field.
     */
    public double getPrice() {
        return this.price;
    }

    /**
     *  The getStock method returns the quantity in stock
     *  @return The value in the stock field.
     */
    public int getStock() {
        return this.stock;
    }

    /**
     *  The getMin method returns the product's min value
     *  @return The value in the min field.
     */
    public int getMin() {
        return this.min;
    }

    /**
     *  The getMax method returns the product's max value
     *  @return The value in the max field.
     */
    public int getMax() {
        return this.max;
    }

    /**
     *  The setId method sets the value of the id field.
     *  @param id Specifies the product's id.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     *  The setName method sets the value of the name field.
     *  @param name Specifies the product's name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *  The setPrice method sets the value of the price field.
     *  @param price Specifies the product's price.
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     *  The setStock method sets the value of the stock field.
     *  @param stock Specifies the product's quantity available.
     */
    public void setStock(int stock) {
        this.stock = stock;
    }

    /**
     *  The setMin method sets the value of the min field.
     *  @param min Specifies the product's min value.
     */
    public void setMin(int min) {
        this.min = min;
    }

    /**
     *  The setMax method sets the value of the max field.
     *  @param max Specifies the product's max value.
     */
    public void setMax(int max) {
        this.max = max;
    }

    /**
     *  The addAssociatedPart method adds a part to the associatedPart list.
     *  @param part Specifies the part to be associated with the current product.
     */
    public void addAssociatedPart(Part part) {
        associatedParts.add(part);
    }

    /**
     *  The deleteAssociatedPart method deletes a part from the associatedPart list.
     *  @param part Specifies the part to be deleted from the current product's
     *              associatedPart list.
     */
    public void deleteAssociatedPart(Part part) {
        associatedParts.remove(part);
    }

    /**
     *  The getAllAssociatedParts method returns the list of associated parts
     *  @return The associatedParts list.
     */
    public ObservableList<Part> getAllAssociatedParts() {
        return this.associatedParts;
    }
}
