package com.example.services.notifications.listeners;

import com.example.services.notifications.NotificationService;
import com.example.services.notifications.events.WorkspaceConfitmationEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class WorkspaceNotificationListener {

    private final NotificationService notificationService;

    public WorkspaceNotificationListener(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @EventListener
    public void onWorkspaceCreated(WorkspaceConfitmationEvent event) {
        notificationService.sendWorkspaceConfirmation(event.getWorkspace());
    }
}
