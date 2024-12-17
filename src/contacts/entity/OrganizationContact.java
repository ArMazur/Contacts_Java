package contacts.entity;

import contacts.builders.ContactBuilder;
import contacts.builders.OrganizationContactBuilder;

import java.io.Serial;
import java.io.Serializable;

/**
 * The OrganizationContact class represents a contact for an organization.
 * <p>
 * It extends the abstract Contact class and adds an attribute for the organization's address.
 * The class also maintains a reference to the associated OrganizationContactBuilder.
 * </p>
 */
public class OrganizationContact extends Contact implements Serializable {

    @Serial
    private static final long serialVersionUID = 3L;

    private String address;
    private final OrganizationContactBuilder contactBuilder;

    /**
     * Constructs an OrganizationContact object with the specified details.
     *
     * @param name           The name of the organization.
     * @param address        The address of the organization.
     * @param number         The phone number of the organization.
     * @param contactBuilder The builder instance used to construct this contact.
     */
    public OrganizationContact(String name, String address, String number, OrganizationContactBuilder contactBuilder) {
        super(name, number, contactBuilder);
        this.address = address;
        this.contactBuilder = contactBuilder;
    }

    /**
     * Retrieves the address of the organization.
     *
     * @return The address as a String.
     */
    public String getAddress() {
        return address;
    }

    /**
     * Retrieves the ContactBuilder instance associated with this contact.
     *
     * @return The ContactBuilder instance.
     */
    @Override
    public ContactBuilder getContactBuilder() {
        return contactBuilder;
    }

    /**
     * Returns a String representation of the organization contact, including all its details.
     *
     * @return A formatted String containing the organization's name, address, phone number,
     * and timestamps for creation and last edit.
     */
    @Override
    public String toString() {
        return "Organization name: " + getName() + "\n" +
                "Address: " + getAddress() + "\n" +
                "Number: " + getNumber() + "\n" +
                "Time created: " + getTimeCreated() + "\n" +
                "Time last edit: " + getTimeUpdated() + "\n";
    }
}
