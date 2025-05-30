package com.example.services.notifications.events;

import com.example.domain.entities.Workspace;

public class WorkspaceConfitmationEvent {

    private final Workspace workspace;

    public WorkspaceConfitmationEvent(Workspace workspace) {
        this.workspace = workspace;
    }

    public Workspace getWorkspace() {
        return workspace;
    }
}
