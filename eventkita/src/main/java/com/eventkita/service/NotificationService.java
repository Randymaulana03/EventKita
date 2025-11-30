package com.eventkita.service;

import com.eventkita.entity.Notification;
import com.eventkita.entity.User;

import java.util.List;
import java.util.Optional;

public interface NotificationService {
    Notification createNotification(User user, String title, String message);
    List<Notification> getUserNotifications(User user);
    Optional<Notification> markAsRead(Long notificationId, User user);
    long getUnreadCount(User user);
}
