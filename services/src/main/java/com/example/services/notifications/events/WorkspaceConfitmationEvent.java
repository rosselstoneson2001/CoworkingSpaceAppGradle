package com.example.services.notifications.events;

import com.example.domain.entities.Workspace;

/**
 * Event class that represents a workspace confirmation event.
 *
 * This event is used to notify listeners when a workspace has been created or updated
 * and a confirmation needs to be sent. The event encapsulates the {@link Workspace}
 * entity, which contains all the details necessary for the confirmation process.
 *
 * Listeners can subscribe to this event to perform actions like sending a workspace
 * confirmation email or logging the details of the workspace.
 */
public class WorkspaceConfitmationEvent {

    private final Workspace workspace;

    public WorkspaceConfitmationEvent(Workspace workspace) {
        this.workspace = workspace;
    }

    public Workspace getWorkspace() {
        return workspace;
    }
}
