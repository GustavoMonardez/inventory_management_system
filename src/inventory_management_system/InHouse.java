package inventory_management_system;

/**
 The InHouse class inherits from the Part class and extends its functionality.
 * @author Gustavo Monardez
 */
public class InHouse extends Part{
    private int machineID;

    /**
     * The constructor calls the base class constructor to initialize
     * common properties and it initializes the machineID
     * @param id The part's ID
     * @param name The part's name
     * @param price The part's price
     * @param stock Quantity in stock
     * @param min Minimum value
     * @param max Maximum value
     * @param machineID The part's machineID
     */
    InHouse(int id, String name, double price, int stock, int min, int max, int machineID) {
        super(id, name, price, stock, min, max);
        this.machineID = machineID;
    }

    /**
     *  The getMachineID method returns the machine ID.
     *  @return The value in the machineID field.
     */
    int getMachineID() {
        return this.machineID;
    }

    /**
     *  This method sets the value of the machineID field.
     *  @param machineID Specifies the machine ID.
     */
    void setMachineID(int machineID) {
        this.machineID = machineID;
    }
}
