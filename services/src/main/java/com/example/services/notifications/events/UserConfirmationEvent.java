package com.example.services.notifications.events;

import com.example.domain.entities.User;

/**
 * Event class that represents a user confirmation event.
 *
 * This event is used to notify listeners when a user has been created or updated
 * and a confirmation needs to be sent. The event encapsulates the {@link User}
 * entity, which contains all the details necessary for the confirmation process.
 *
 * Listeners can subscribe to this event to perform actions like sending a user
 * confirmation email or logging the details of the user.
 */
public class UserConfirmationEvent {

    private final User user;

    public UserConfirmationEvent(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
