package contacts.utils;

import contacts.builders.ContactBuilder;
import contacts.entity.Contact;

import java.lang.reflect.Field;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The ContactSearcher class provides functionality to search through a list of contacts
 * based on a query. It dynamically generates searchable strings for each contact using reflection.
 */
public class ContactSearcher {
    private static final Scanner SCANNER = new Scanner(System.in);

    /**
     * Default constructor for ContactSearcher.
     */
    public ContactSearcher() {
    }

    /**
     * Prompts the user for a search query and returns a list of matching contacts.
     *
     * @param contactList The list of contacts to search within.
     * @return A list of Contact objects that match the query.
     */
    public List<Contact> searchContact(List<Contact> contactList) {
        System.out.print("Enter search query: ");
        String query = SCANNER.nextLine();
        return searchByQuery(query, contactList);
    }

    /**
     * Searches for contacts that match the given query string.
     *
     * @param query       The search query entered by the user.
     * @param contactList The list of contacts to search within.
     * @return A list of contacts that match the query.
     */
    private List<Contact> searchByQuery(String query, List<Contact> contactList) {
        var dataMap = generateDataMap(contactList);

        // Escape special characters to treat the query as a literal string
        String escapedQuery = Pattern.quote(query);
        Pattern pattern = Pattern.compile(".*" + escapedQuery + ".*", Pattern.CASE_INSENSITIVE);

        List<Contact> searchResultsList = new ArrayList<>();

        dataMap.forEach((contact, dataString) -> {
            Matcher m = pattern.matcher(dataString);
            if (m.find()) {
                searchResultsList.add(contact);
            }
        });
        return searchResultsList;
    }

    /**
     * Generates a map of contacts and their corresponding data strings used for searching.
     *
     * @param contactList The list of contacts to process.
     * @return A Map where the key is the Contact object and the value is its generated data string.
     */
    private Map<Contact, String> generateDataMap(List<Contact> contactList) {
        return contactList.stream()
                .collect(
                        HashMap::new,
                        (map, contact) -> map.put(contact, generateDataString(contact)),
                        HashMap::putAll
                );
    }

    /**
     * Dynamically generates a searchable data string for a contact using reflection.
     * <p>
     * It excludes fields like "timeCreated" and "timeUpdated" and ignores any field
     * implementing or extending the ContactBuilder interface.
     * </p>
     *
     * @param contact The Contact object to generate the data string for.
     * @return A concatenated string containing the values of all searchable fields.
     */
    private String generateDataString(Contact contact) {
        StringBuilder sb = new StringBuilder();
        Class<?> currentClass = contact.getClass();

        while (currentClass != null) {
            Field[] fields = currentClass.getDeclaredFields();
            for (var field : fields) {
                field.setAccessible(true);
                // Exclude timeCreated, timeUpdated, and ContactBuilder-related fields
                if (!field.getName().equals("timeCreated") &&
                        !field.getName().equals("timeUpdated") &&
                        !ContactBuilder.class.isAssignableFrom(field.getType())) {
                    try {
                        Object value = field.get(contact);
                        if (value != null) {
                            sb.append(value);
                        }
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            currentClass = currentClass.getSuperclass();
        }
        return sb.toString().trim();
    }
}
