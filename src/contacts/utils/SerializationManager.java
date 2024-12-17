package contacts.utils;

import contacts.entity.Contact;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * The SerializationManager class is responsible for managing the serialization and deserialization
 * of contact data to and from a file.
 * <p>
 * It implements the Singleton pattern to ensure that only one instance of the manager exists
 * throughout the application.
 * </p>
 */
public class SerializationManager {

    private String fileName;
    private static SerializationManager instance;
    private List<Contact> contactsFromFile;

    /**
     * Private constructor to enforce the Singleton pattern.
     */
    private SerializationManager() {
    }

    /**
     * Retrieves the single instance of SerializationManager.
     *
     * @return The Singleton instance of SerializationManager.
     */
    public static SerializationManager getInstance() {
        if (instance == null) {
            instance = new SerializationManager();
        }
        return instance;
    }

    /**
     * Sets the name of the file used for saving and loading contact data.
     *
     * @param fileName The name of the file (without extension).
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Generates the full file path for storing serialized contact data.
     *
     * @return The file path as a String.
     */
    private String getFileOutputPath() {
        return System.getProperty("user.dir") + "/src/contacts/data/" + fileName + ".ser";
    }

    /**
     * Saves the provided list of contacts to a file using serialization.
     *
     * @param contactList The list of contacts to save.
     */
    public void saveContacts(List<Contact> contactList) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(getFileOutputPath()))) {
            oos.writeObject(contactList);
        } catch (IOException e) {
            System.out.println("Problem in saving contacts to a file!");
            e.printStackTrace();
        }
    }

    /**
     * Retrieves the list of contacts by loading them from the serialized file.
     *
     * @return A list of Contact objects, or null if loading fails.
     */
    public List<Contact> getContactsFromFile() {
        loadContacts();
        return contactsFromFile;
    }

    /**
     * Loads the contacts from the serialized file and stores them in the contactsFromFile field.
     * <p>
     * It verifies the existence of the file and ensures the deserialized object is a valid List of Contact objects.
     * </p>
     */
    private void loadContacts() {
        if (Files.exists(Path.of(getFileOutputPath()))) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(getFileOutputPath()))) {

                Object obj = ois.readObject();

                if (obj instanceof List<?> tempList) { // Check if the object is a List
                    if (tempList.isEmpty() || tempList.get(0) instanceof Contact) { // Validate list type
                        //noinspection unchecked
                        contactsFromFile = (List<Contact>) tempList; // gets a List<Contact> from serialization data
                    } else {
                        System.out.println("Deserialized list is not a List<Contact>");
                    }
                } else {
                    System.out.println("Deserialized object is not a List<?>!");
                }

            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Problem in loading contacts from file: " + getFileOutputPath());
            }
        } else {
            System.out.println(getFileOutputPath() + " file doesn't exist");
        }
    }
}
