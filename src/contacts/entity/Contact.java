package contacts.entity;

import contacts.builders.ContactBuilder;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * The Contact class serves as an abstract base class for all contact types.
 * <p>
 * It holds common attributes like name, phone number, creation time, and update time.
 * It also maintains a reference to the corresponding ContactBuilder for the contact.
 * </p>
 */
public abstract class Contact implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String name;
    private String number;
    private final LocalDateTime timeCreated;
    private LocalDateTime timeUpdated;
    private final ContactBuilder contactBuilder;

    /**
     * Constructs a Contact object with the specified name, phone number, and contact builder.
     *
     * @param name            The name of the contact.
     * @param number          The phone number of the contact.
     * @param contactBuilder  The builder instance used to construct this contact.
     */
    public Contact(String name, String number, ContactBuilder contactBuilder) {
        this.name = name;
        this.number = number;
        this.timeCreated = LocalDateTime.now();
        this.timeUpdated = LocalDateTime.now();
        this.contactBuilder = contactBuilder;
    }

    /**
     * Retrieves the name of the contact.
     *
     * @return The contact's name as a String.
     */
    public String getName() {
        return name;
    }

    /**
     * Retrieves the phone number of the contact.
     *
     * @return The contact's phone number as a String.
     */
    public String getNumber() {
        return number;
    }

    /**
     * Retrieves the time when the contact was created.
     *
     * @return The creation time, truncated to minutes.
     */
    public LocalDateTime getTimeCreated() {
        return timeCreated.truncatedTo(ChronoUnit.MINUTES);
    }

    /**
     * Retrieves the last updated time for the contact.
     *
     * @return The last update time, truncated to minutes.
     */
    public LocalDateTime getTimeUpdated() {
        return timeUpdated.truncatedTo(ChronoUnit.MINUTES);
    }

    /**
     * Updates the last updated time of the contact.
     *
     * @param timeUpdated The new update time.
     */
    public void setTimeUpdated(LocalDateTime timeUpdated) {
        this.timeUpdated = timeUpdated;
    }

    /**
     * Retrieves the ContactBuilder associated with this contact.
     *
     * @return The ContactBuilder instance.
     */
    public ContactBuilder getContactBuilder() {
        return contactBuilder;
    }
}
