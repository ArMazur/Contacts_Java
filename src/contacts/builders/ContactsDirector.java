package contacts.builders;

import contacts.entity.Contact;
import contacts.entity.PersonContact;
import contacts.utils.ContactSearcher;
import contacts.utils.SerializationManager;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

/**
 * The ContactsDirector class manages the creation, editing, deletion, searching,
 * and serialization of contact records.
 * <p>
 * This class uses the Builder design pattern for constructing contacts and reflection
 * for dynamically editing contact fields.
 * </p>
 */
public class ContactsDirector {
    private static final Scanner SCANNER = new Scanner(System.in);
    private final PersonContactBuilder personContactBuilder;
    private final OrganizationContactBuilder organizationContactBuilder;
    private List<Contact> contacts = new ArrayList<>();
    private List<Contact> searchedContacts = new ArrayList<>();

    /**
     * Initializes a ContactsDirector with builders for Person and Organization contacts.
     */
    public ContactsDirector() {
        this.personContactBuilder = new PersonContactBuilder();
        this.organizationContactBuilder = new OrganizationContactBuilder();
    }

    /**
     * Prompts the user to choose the type of contact (person or organization).
     *
     * @return The chosen contact type as a String, or null if invalid input.
     */
    public String chooseType() {
        System.out.print("Enter the type (person, organization): ");
        String input = SCANNER.nextLine();
        if (checkType(input)) {
            return input;
        }
        System.out.println("Wrong type, choose the correct type!\n");
        return null;
    }

    /**
     * Validates if the input is a valid contact type.
     *
     * @param input The user's input.
     * @return True if input is "person" or "organization", otherwise false.
     */
    private boolean checkType(String input) {
        return input.equals("person") || input.equals("organization");
    }

    /**
     * Creates a new contact based on the specified type and saves it to the file.
     *
     * @param type The type of contact to create ("person" or "organization").
     */
    public void createNewContact(String type) {
        Contact contact = switch (type) {
            case "person" -> createNewPersonContact(personContactBuilder);
            case "organization" -> createNewOrganizationContact(organizationContactBuilder);
            default -> null;
        };
        contacts.add(contact);
        saveContactsToFile();
        System.out.println("The record added.\n");
    }


    /**
     * Constructs a new PersonContact using the builder pattern.
     *
     * @param personContactBuilder The builder for PersonContact.
     * @return A new PersonContact instance.
     */
    private Contact createNewPersonContact(PersonContactBuilder personContactBuilder) {
        personContactBuilder.reset()
                .addName()
                .addSurname()
                .addBirthDate()
                .addGender()
                .addNumber();
        return personContactBuilder.getContact();
    }

    /**
     * Constructs a new OrganizationContact using the builder pattern.
     *
     * @param organizationContactBuilder The builder for OrganizationContact.
     * @return A new OrganizationContact instance.
     */
    private Contact createNewOrganizationContact(OrganizationContactBuilder organizationContactBuilder) {
        organizationContactBuilder.reset()
                .addName()
                .addAddress()
                .addNumber();
        return organizationContactBuilder.getContact();
    }

    /**
     * Displays a list of all contacts with their indices.
     *
     * @return True if there are contacts to display; otherwise, false.
     */
    public boolean listAllContacts() {
        if (contacts.isEmpty()) {
            System.out.println("No contacts to show info!\n");
            return false;
        } else {
            printContactsNames(contacts);
            return true;
        }
    }

    /**
     * Prints the names of the provided contacts with their indices.
     *
     * @param contacts The list of contacts to display.
     */
    private void printContactsNames(List<Contact> contacts) {
        contacts.forEach(contact -> {
            String contactName = contact instanceof PersonContact ? ((PersonContact) contact).getFullName() : contact.getName();
            System.out.printf("%d. %s%n", contacts.indexOf(contact) + 1, contactName);
        });
        System.out.println();
    }

    /**
     * Prompts the user to select a contact and displays its details.
     *
     * @return The index of the selected contact.
     */
    public int selectContact() {
        int record = selectValidRecord(contacts);
        printSelectedContact(record - 1);
        return record;
    }


    /**
     * Displays details of the contact at the specified index.
     *
     * @param index The index of the contact in the list.
     */
    private void printSelectedContact(int index) {
        System.out.println(contacts.get(index));
    }

    /**
     * Displays the details of a contact from a list of searched contacts.
     *
     * @param index    The index of the contact in the searched list.
     * @param contacts The list of contacts to search within.
     */
    private void printSelectedContactFromList(int index, List<Contact> contacts) {
        System.out.println(contacts.get(index));
    }

    /**
     * Prompts the user to select a valid record from a list of contacts.
     *
     * @param contacts The list of contacts to select from.
     * @return The index of the selected record.
     */
    private int selectValidRecord(List<Contact> contacts) {
        int record;
        do {
            System.out.print("Select a record: ");
            record = SCANNER.nextInt();
        } while (invalidIndex(record, contacts));
        return record;
    }

    /**
     * Checks if the provided index is valid for the given list of contacts.
     *
     * @param record   The index to validate.
     * @param contacts The list of contacts to check against.
     * @return True if the index is invalid; otherwise, false.
     */
    private boolean invalidIndex(int record, List<Contact> contacts) {
        try {
            contacts.get(record - 1);
        } catch (IndexOutOfBoundsException e) {
            return true;
        }
        return false;
    }

    /**
     * Retrieves a contact by its record index.
     *
     * @param record     The record index.
     * @param isSearched True if retrieving from a search result; otherwise, false.
     * @return The Contact object at the specified index.
     */
    public Contact getContact(int record, boolean isSearched) {
        int index = record - 1;
        if (isSearched) {
            var contact = searchedContacts.get(index);
            index = contacts.indexOf(contact);
        }
        return contacts.get(index);
    }

    /**
     * Displays the total number of contacts in the list.
     */
    public void getContactsSize() {
        System.out.printf("The Phone Book has %d records.%n%n", contacts.size());
    }

    /**
     * Edits an existing contact by dynamically updating its fields using reflection.
     *
     * @param contact The contact to be edited.
     */
    public void editContact(Contact contact) {
        int index = contacts.indexOf(contact);
        ContactBuilder contactBuilder = contact.getContactBuilder();
        contact = editContactInfo(contact, contactBuilder);
        contact.setTimeUpdated(LocalDateTime.now());
        contacts.set(index, contact);
        saveContactsToFile();
        System.out.println("The record updated!\n");
    }

    /**
     * Updates a contact's field by dynamically invoking the appropriate "add" method in
     * appropriate ContactBuilder, using reflection.
     *
     * @param contact        The contact to be edited.
     * @param contactBuilder The ContactBuilder instance used to rebuild the contact.
     * @return The updated Contact object.
     * @throws IllegalArgumentException If the reflective method invocation fails.
     */
    private Contact editContactInfo(Contact contact, ContactBuilder contactBuilder) {
        List<String> fieldsList = getAvailableFields(contact.getClass());
        String field = getFieldToEdit(fieldsList);
        String methodName = "add" + Character.toUpperCase(field.charAt(0)) + field.substring(1);

        try {
            Method methodFromBuilder = contactBuilder.getClass().getMethod(methodName);
            methodFromBuilder.invoke(contactBuilder);
            contact = contactBuilder.getContact();
        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            throw new IllegalArgumentException("Error invoking method '" + methodName + "' in editContactInfo method", e);
        }
        return contact;
    }

    /**
     * Retrieves a list of editable fields from the specified class, excluding irrelevant or technical fields.
     *
     * @param clazz The Class object representing the contact's class.
     * @param <T>   The type of the class.
     * @return A list of field names available for editing.
     */
    private <T> List<String> getAvailableFields(Class<T> clazz) {
        List<String> fields = new ArrayList<>();
        Class<?> currentClass = clazz;

        while (currentClass != null) {
            for (var field : currentClass.getDeclaredFields()) {
                if (!field.getName().equals("timeCreated") &&
                        !field.getName().equals("timeUpdated") &&
                        !field.getName().equals("serialVersionUID")) {
                    fields.add(field.getName());
                }
            }
            currentClass = currentClass.getSuperclass();
        }
        return fields;
    }

    /**
     * Prompts the user to select a field to edit from the list of available fields.
     *
     * @param availableFields A list of field names available for editing.
     * @return The field name chosen by the user.
     */
    private String getFieldToEdit(List<String> availableFields) {
        String fields = String.join(", ", availableFields);
        System.out.print("Select a field (" + fields + "): ");
        String field = SCANNER.next();
        while (!availableFields.contains(field)) {
            System.out.print("Select a field (" + fields + "): ");
            field = SCANNER.next();
        }
        return field;
    }

    /**
     * Removes a contact from the list of contacts and updates the serialized file.
     *
     * @param contact The contact to be removed.
     */
    public void removeContact(Contact contact) {
        if (contacts.remove(contact)) {
            System.out.println("The record removed!");
            saveContactsToFile();
        }
    }

    /**
     * Saves the list of contacts to a file using SerializationManager.
     */
    public void saveContactsToFile() {
        SerializationManager.getInstance().saveContacts(contacts);
    }

    /**
     * Loads contacts from the serialized file into the list.
     */
    public void loadContactsFromFile() {
        var contactsFromFile = SerializationManager.getInstance().getContactsFromFile();
        contacts = Objects.requireNonNullElseGet(contactsFromFile, ArrayList::new);
    }

    /**
     * Searches for contacts based on user input and displays matching results.
     */
    public void search() {
        ContactSearcher contactSearcher = new ContactSearcher();
        searchedContacts = contactSearcher.searchContact(contacts);
        if (!searchedContacts.isEmpty()) {
            System.out.println("Found " + searchedContacts.size() + " results:");
            listContacts(searchedContacts);
        } else {
            System.out.println("No contacts found!");
        }
    }

    /**
     * Displays a list of contacts.
     *
     * @param contacts The list of contacts to display.
     */
    private void listContacts(List<Contact> contacts) {
        if (contacts.isEmpty()) {
            System.out.println("No contacts to show info!");
        } else {
            printContactsNames(contacts);
        }
    }

    /**
     * Prints the details of a specific searched contact based on the given record number.
     *
     * @param record The index number of the contact to display (1-based index).
     * @return True if the contact exists and is displayed; otherwise, false.
     */
    public boolean printSearchedContact(int record) {
        try {
            printSelectedContactFromList(record - 1, searchedContacts);
            return true;
        } catch (Exception e) {
            System.out.println("No such record!");
            return false;
        }
    }
}
