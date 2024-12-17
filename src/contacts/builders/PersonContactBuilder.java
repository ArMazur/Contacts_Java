package contacts.builders;

import contacts.entity.PersonContact;

import java.io.Serial;
import java.io.Serializable;
import java.util.Scanner;

/**
 * The PersonContactBuilder class is responsible for constructing a PersonContact object.
 * <p>
 * It follows the Builder pattern and interacts with the user via console input
 * to collect information such as name, surname, birthdate, gender, and phone number.
 * </p>
 */
public class PersonContactBuilder implements ContactBuilder, Serializable {

    @Serial
    private static final long serialVersionUID = 589221725612290269L;

    private String name;
    private String surname;
    private String birthDate;
    private String gender;
    private String number = "[no number]";
    private static final Scanner SCANNER = new Scanner(System.in);

    /**
     * Default constructor for PersonContactBuilder.
     */
    public PersonContactBuilder() {
    }

    /**
     * Prompts the user to enter a name and sets it.
     *
     * @return The current PersonContactBuilder instance.
     */
    @Override
    public PersonContactBuilder addName() {
        System.out.print("Enter the name: ");
        name = SCANNER.nextLine();
        return this;
    }

    /**
     * Prompts the user to enter a surname and sets it.
     *
     * @return The current PersonContactBuilder instance.
     */
    public PersonContactBuilder addSurname() {
        System.out.print("Enter the surname: ");
        surname = SCANNER.nextLine();
        return this;
    }

    /**
     * Prompts the user to enter a birthdate and sets it with validation.
     *
     * @return The current PersonContactBuilder instance.
     */
    public PersonContactBuilder addBirthDate() {
        System.out.print("Enter the birth date: ");
        setBirthDate(SCANNER.nextLine());
        return this;
    }

    /**
     * Sets the birthdate with a validation check.
     *
     * @param birthDate The birthdate entered by the user.
     */
    private void setBirthDate(String birthDate) {
        if (!birthDate.isBlank()) {
            this.birthDate = birthDate;
        } else {
            System.out.println("Bad birth date!");
            this.birthDate = "[no data]";
        }
    }

    /**
     * Prompts the user to enter a gender (M/F) and sets it with validation.
     *
     * @return The current PersonContactBuilder instance.
     */
    public PersonContactBuilder addGender() {
        System.out.print("Enter the gender (M, F): ");
        setGender(SCANNER.nextLine());
        return this;
    }

    /**
     * Sets the gender with a validation check.
     *
     * @param gender The gender entered by the user.
     */
    private void setGender(String gender) {
        if (gender.equals("M") || gender.equals("F")) {
            this.gender = gender;
        } else {
            System.out.println("Bad gender!");
            this.gender = "[no data]";
        }
    }

    /**
     * Prompts the user to enter a phone number and sets it with validation.
     *
     * @return The current PersonContactBuilder instance.
     */
    @Override
    public PersonContactBuilder addNumber() {
        System.out.print("Enter the number: ");
        setNumber(SCANNER.nextLine());
        return this;
    }

    /**
     * Sets the phone number after validating it using the checkNumber method.
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
     * Resets the builder fields to their default values.
     *
     * @return The current PersonContactBuilder instance.
     */
    @Override
    public PersonContactBuilder reset() {
        this.name = "";
        this.surname = "";
        this.number = "[no number]";
        return this;
    }

    /**
     * Retrieves the name currently set in the builder.
     *
     * @return The name as a String.
     */
    public String getName() {
        return name;
    }

    /**
     * Retrieves the phone number currently set in the builder.
     *
     * @return The phone number as a String.
     */
    public String getNumber() {
        return number;
    }

    /**
     * Builds and returns a PersonContact object using the data collected in the builder.
     *
     * @return A new PersonContact instance.
     */
    public PersonContact getContact() {
        return new PersonContact(name, surname, birthDate, gender, number, this);
    }
}
