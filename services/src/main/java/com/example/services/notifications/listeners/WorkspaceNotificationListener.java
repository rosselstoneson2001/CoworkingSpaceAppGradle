package com.example.services.notifications.listeners;

import com.example.services.notifications.NotificationService;
import com.example.services.notifications.events.WorkspaceConfitmationEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Listener class for handling workspace creation events and triggering corresponding
 * notification actions.
 *
 * This class listens to events related to workspace creation (such as the
 * {@link WorkspaceConfitmationEvent}) and uses the {@link NotificationService}
 * to send a workspace confirmation notification to the appropriate recipient.
 *
 * The listener is triggered by events of type {@link WorkspaceConfitmationEvent}
 * and sends a workspace confirmation notification via the NotificationService.
 */
@Component
public class WorkspaceNotificationListener {

    private final NotificationService notificationService;

    public WorkspaceNotificationListener(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    /**
     * Event listener method that is triggered when a {@link WorkspaceConfitmationEvent}
     * is published. This method sends a workspace confirmation notification.
     *
     * @param event the WorkspaceConfitmationEvent that contains the workspace details
     */
    @EventListener
    public void onWorkspaceCreated(WorkspaceConfitmationEvent event) {
        notificationService.sendWorkspaceConfirmation(event.getWorkspace());
    }
}
