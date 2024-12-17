package contacts.entity;

import contacts.builders.ContactBuilder;
import contacts.builders.PersonContactBuilder;

import java.io.Serial;
import java.io.Serializable;

/**
 * The PersonContact class represents a contact for an individual person.
 * <p>
 * It extends the abstract Contact class and adds additional attributes such as
 * surname, birthdate, and gender. It includes functionality to retrieve a full name.
 * </p>
 */
public final class PersonContact extends Contact implements Serializable {

    @Serial
    private static final long serialVersionUID = 2L;

    private String surname;
    private String birthDate;
    private String gender;
    private final PersonContactBuilder contactBuilder;

    /**
     * Constructs a PersonContact object with the specified details.
     *
     * @param name           The first name of the contact.
     * @param surname        The surname of the contact.
     * @param birthDate      The birthdate of the contact.
     * @param gender         The gender of the contact (M/F).
     * @param number         The phone number of the contact.
     * @param contactBuilder The builder instance used to construct this contact.
     */
    public PersonContact(String name, String surname, String birthDate, String gender, String number, PersonContactBuilder contactBuilder) {
        super(name, number, contactBuilder);
        this.surname = surname;
        this.birthDate = birthDate;
        this.gender = gender;
        this.contactBuilder = contactBuilder;
    }

    /**
     * Retrieves the surname of the contact.
     *
     * @return The surname as a String.
     */
    public String getSurname() {
        return surname;
    }

    /**
     * Retrieves the full name of the contact (first name + surname).
     *
     * @return The full name as a single concatenated String.
     */
    public String getFullName() {
        return this.getName() + " " + this.getSurname();
    }

    /**
     * Retrieves the birthdate of the contact.
     *
     * @return The birthdate as a String.
     */
    public String getBirthDate() {
        return birthDate;
    }

    /**
     * Retrieves the gender of the contact.
     *
     * @return The gender as a String.
     */
    public String getGender() {
        return gender;
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
     * Returns a String representation of the contact, including all its details.
     *
     * @return A formatted String containing the contact's name, surname, birthdate,
     * gender, phone number, and timestamps for creation and last edit.
     */
    @Override
    public String toString() {
        return "Name: " + getName() + "\n" +
                "Surname: " + getSurname() + "\n" +
                "birthdate: " + getBirthDate() + "\n" +
                "Gender: " + getGender() + "\n" +
                "Number: " + getNumber() + "\n" +
                "Time created: " + getTimeCreated() + "\n" +
                "Time last edit: " + getTimeUpdated() + "\n";
    }
}
