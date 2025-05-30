package com.example.services.notifications.events;

import com.example.domain.entities.User;

public class UserConfirmationEvent {

    private final User user;

    public UserConfirmationEvent(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
