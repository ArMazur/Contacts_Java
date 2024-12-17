package contacts.builders;

import contacts.entity.Contact;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The ContactBuilder interface defines the blueprint for building contact objects.
 * It uses the Builder design pattern to incrementally construct a Contact entity.
 */
public interface ContactBuilder extends Serializable {

    /**
     * Regular expression pattern for validating phone numbers.
     * <p>
     * This pattern supports various formats, including optional "+" signs, numbers with or without parentheses,
     * and hyphen/space-separated groups.
     * </p>
     */

    // @formatter:off
    Pattern PATTERN =
            Pattern.compile(
                    "^\\+?" +                                                                             // optional "+" sign
                            "([0-9a-z]|" +                                                                      // only one character
                            "([0-9a-z][-\\s])?(\\([0-9a-z]{2,}\\))([-\\s][0-9a-z]{2,})*|" +                     // when number has () in first group
                            "([0-9a-z][-\\s])?([0-9a-z]{2,})([-\\s][0-9a-z]{2,})*|" +                           // when number has NO ()
                            "([0-9a-z][-\\s])?([0-9a-z]{2,})([-\\s]\\([0-9a-z]{2,}\\))([-\\s][0-9a-z]{2,})*)$", // when number has only second group with ()
                    Pattern.CASE_INSENSITIVE);
    // @formatter:on

    /**
     * Adds a name to the contact being built.
     *
     * @return The current instance of ContactBuilder for method chaining.
     */
    ContactBuilder addName();

    /**
     * Retrieves the name of the contact.
     *
     * @return The name of the contact as a String.
     */
    String getName();

    /**
     * Adds a phone number to the contact being built.
     *
     * @return The current instance of ContactBuilder for method chaining.
     */
    ContactBuilder addNumber();

    /**
     * Retrieves the phone number of the contact.
     *
     * @return The phone number of the contact as a String.
     */
    String getNumber();

    /**
     * Resets the builder to its initial state.
     *
     * @return The current instance of ContactBuilder after reset.
     */
    ContactBuilder reset();

    /**
     * Checks if the provided phone number matches the valid phone number pattern.
     *
     * @param number The phone number string to validate.
     * @return True if the phone number matches the pattern; otherwise, false.
     */
    default boolean checkNumber(String number) {
        Matcher matcher = PATTERN.matcher(number);
        return matcher.matches();
    }

    /**
     * Builds and retrieves the contact object.
     *
     * @return The fully constructed Contact object.
     */
    Contact getContact();
}
