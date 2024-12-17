package contacts.main;

import contacts.utils.SerializationManager;

/**
 * The Main class serves as the entry point for the application.
 * It initializes the application and manages the filename used for contact serialization.
 */
public class Main {
    public static void main(String[] args) {
        // Set a file name from command line arguments for serialization of contacts
        setFileName(args);

        // Start the application
        new Menu().startApplication();
    }

    /**
     * Sets the file name for serialization of contacts.
     * If no file name is provided as a command line argument, the default name "Contacts" is used.
     * The file name is passed to the SerializationManager for handling serialization.
     *
     * @param args Command line arguments provided when running the application.
     */
    private static void setFileName(String[] args) {
        String fileName = args.length > 0 ? args[0] : "Contacts";
        SerializationManager.getInstance().setFileName(fileName);
    }
}
