package inventory_management_system;

/**
 The Outsourced class inherits from the Part class and extends its functionality.
 * @author Gustavo Monardez
 */
public class Outsourced extends Part{
    private String companyName;

    /**
     * The constructor calls the base class constructor to initialize
     * common properties and it initializes the companyName
     * @param id The part's ID
     * @param name The part's name
     * @param price The part's price
     * @param stock Quantity in stock
     * @param min Minimum value
     * @param max Maximum value
     * @param companyName The name of the company that the part comes from
     */
    Outsourced(int id, String name, double price, int stock, int min, int max, String companyName) {
        super(id, name, price, stock, min, max);
        this.companyName = companyName;
    }

    /**
     *  The getCompanyName method returns the company's name
     *  that the part comes from.
     *  @return The value in the companyName field.
     */
    String getCompanyName() {
        return this.companyName;
    }

    /**
     *  This method sets the value of the companyName field.
     *  @param companyName Specifies the company name.
     */
    void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
