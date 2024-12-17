package contacts.main;

import contacts.builders.ContactsDirector;
import contacts.entity.Contact;

import java.util.Scanner;

/**
 * The Menu class provides an interactive console menu for managing contacts.
 * It allows users to add, list, search, count, and exit the application.
 */
public class Menu {

    private static final Scanner SCANNER = new Scanner(System.in);
    private final ContactsDirector contactsDirector = new ContactsDirector();

    /**
     * Default constructor for the Menu class.
     */
    public Menu() {
    }

    /**
     * Starts the contact management application by loading contacts and displaying the main menu.
     */
    public void startApplication() {
        contactsDirector.loadContactsFromFile();
        chooseAction();
    }

    /**
     * Displays the main menu and prompts the user to choose actions until "exit" is entered.
     */
    private void chooseAction() {
        String action = "";
        while (!action.equals("exit")) {
            System.out.print("[menu] Enter action (add, list, search, count, exit): ");
            action = SCANNER.nextLine().trim().toLowerCase();
            if (checkAction(action)) {
                mainMenuAction(action);
            } else {
                System.out.println("Wrong input! Choose action from the list!");
                System.out.println();
            }
        }
    }

    /**
     * Executes the appropriate action based on the user's input.
     *
     * @param action The action entered by the user.
     */
    private void mainMenuAction(String action) {
        switch (action) {
            case "add":
                var type = contactsDirector.chooseType();
                if (type != null) {
                    contactsDirector.createNewContact(type);
                }
                break;
            case "list":
                listMenu();
                break;
            case "search":
                searchMenu();
                break;
            case "count":
                contactsDirector.getContactsSize();
                break;
            case "exit":
                /*
                // Uncomment this code if you want your Contacts.ser file to be deleted after exiting the program
                try {
                    Files.deleteIfExists(Path.of("Contacts.ser"));
                } catch (IOException e) {
                    System.out.println("No file to delete");
                }
                */
                break;
        }
    }

    /**
     * Validates the action entered by the user.
     *
     * @param action The action string to validate.
     * @return True if the action is valid; otherwise, false.
     */
    private boolean checkAction(String action) {
        String allowedActions = "add, list, search, count, exit";
        return allowedActions.contains(action);
    }

    /**
     * Handles the search menu, allowing users to search for contacts and perform actions.
     */
    private void searchMenu() {
        contactsDirector.search();

        System.out.print("[search] Enter action ([number], back, again): ");
        String input = SCANNER.nextLine().trim().toLowerCase();

        if (isNumber(input)) {
            int record = Integer.parseInt(input);
            if (contactsDirector.printSearchedContact(record)) {
                Contact contact = contactsDirector.getContact(record, true);
                recordMenu(contact);
            } else {
                System.out.println("Wrong input!");
            }
        } else {
            switch (input) {
                case "back":
                    chooseAction();
                    break;
                case "again":
                    searchMenu();
                    break;
                default:
                    System.out.println("Wrong input!");
            }
        }
    }

    /**
     * Handles the list menu, displaying all contacts and allowing users to select a record.
     */
    private void listMenu() {
        if (contactsDirector.listAllContacts()) {
            int record = contactsDirector.selectContact();
            var contact = contactsDirector.getContact(record, false);
            recordMenu(contact);
        }
    }

    /**
     * Displays the record menu for a specific contact, allowing actions like edit, delete, or return to the menu.
     *
     * @param contact The contact to manage.
     */
    private void recordMenu(Contact contact) {
        System.out.print("[record] Enter action (edit, delete, menu): ");
        String input = SCANNER.nextLine().trim().toLowerCase();

        switch (input) {
            case "edit":
                contactsDirector.editContact(contact);
                break;
            case "delete":
                contactsDirector.removeContact(contact);
                break;
            case "menu":
                break;
            default:
                System.out.println("Wrong action!");
        }
    }

    /**
     * Checks if the input is a valid number.
     *
     * @param input The input string to validate.
     * @return True if the input is a number; otherwise, false.
     */
    private boolean isNumber(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
