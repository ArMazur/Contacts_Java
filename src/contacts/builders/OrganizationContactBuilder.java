package contacts.builders;

import contacts.entity.OrganizationContact;

import java.io.Serial;
import java.io.Serializable;
import java.util.Scanner;

/**
 * The OrganizationContactBuilder class is responsible for constructing an OrganizationContact object.
 * <p>
 * It follows the Builder pattern and interacts with the user via console input
 * to collect details such as organization name, address, and phone number.
 * </p>
 */
public class OrganizationContactBuilder implements ContactBuilder, Serializable {

    @Serial
    private static final long serialVersionUID = -5546780744513258224L;

    private String organizationName;
    private String organizationAddress;
    private String number;
    private static final Scanner SCANNER = new Scanner(System.in);

    /**
     * Default constructor for OrganizationContactBuilder.
     */
    public OrganizationContactBuilder() {
    }

    /**
     * Prompts the user to enter the organization's name and sets it.
     *
     * @return The current OrganizationContactBuilder instance.
     */
    @Override
    public OrganizationContactBuilder addName() {
        System.out.print("Enter the organization name: ");
        organizationName = SCANNER.nextLine();
        return this;
    }

    /**
     * Retrieves the name of the organization set in the builder.
     *
     * @return The organization name as a String.
     */
    @Override
    public String getName() {
        return organizationName;
    }

    /**
     * Prompts the user to enter the organization's address and sets it.
     *
     * @return The current OrganizationContactBuilder instance.
     */
    public OrganizationContactBuilder addAddress() {
        System.out.print("Enter the address: ");
        setAddress(SCANNER.nextLine());
        return this;
    }

    /**
     * Validates and sets the organization's address.
     *
     * @param organisationAddress The address entered by the user.
     */
    private void setAddress(String organisationAddress) {
        if (!organisationAddress.isBlank()) {
            this.organizationAddress = organisationAddress;
        } else {
            System.out.println("Wrong organization address!");
            this.organizationAddress = "[no data]";
        }
    }

    /**
     * Retrieves the organization's address set in the builder.
     *
     * @return The organization's address as a String.
     */
    public String getAddress() {
        return organizationAddress;
    }

    /**
     * Prompts the user to enter the organization's phone number and sets it.
     *
     * @return The current OrganizationContactBuilder instance.
     */
    @Override
    public OrganizationContactBuilder addNumber() {
        System.out.print("Enter the number: ");
        setNumber(SCANNER.nextLine());
        return this;
    }

    /**
     * Validates and sets the organization's phone number.
     *
     * @param number The phone number entered by the user.
     */
    private void setNumber(String number) {
        if (checkNumber(number) && !number.isBlank()) {
            this.number = number;
        } else {
            System.out.println("Wrong number format!");
            this.number = "[no number]";
        }
    }

    /**
     * Retrieves the phone number set in the builder.
     *
     * @return The organization's phone number as a String.
     */
    @Override
    public String getNumber() {
        return number;
    }

    /**
     * Resets the builder fields to their default values.
     *
     * @return The current OrganizationContactBuilder instance.
     */
    @Override
    public OrganizationContactBuilder reset() {
        this.organizationName = "";
        this.organizationAddress = "";
        this.number = "[no number]";
        return this;
    }

    /**
     * Builds and returns an OrganizationContact object using the data collected in the builder.
     *
     * @return A new OrganizationContact instance.
     */
    public OrganizationContact getContact() {
        return new OrganizationContact(organizationName, organizationAddress, number, this);
    }
}
