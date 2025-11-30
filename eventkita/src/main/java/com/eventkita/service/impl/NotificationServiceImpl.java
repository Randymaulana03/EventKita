package com.eventkita.service.impl;

import com.eventkita.entity.Notification;
import com.eventkita.entity.User;
import com.eventkita.repository.NotificationRepository;
import com.eventkita.service.NotificationService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationServiceImpl(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Override
    public Notification createNotification(User user, String title, String message) {
        Notification n = Notification.builder()
                .user(user)
                .title(title)
                .message(message)
                .read(false)
                .createdAt(LocalDateTime.now())
                .build();
        return notificationRepository.save(n);
    }

    @Override
    public List<Notification> getUserNotifications(User user) {
        return notificationRepository.findByUserOrderByCreatedAtDesc(user);
    }

    @Override
    public Optional<Notification> markAsRead(Long notificationId, User user) {
        return notificationRepository.findById(notificationId)
                .filter(n -> n.getUser().getId().equals(user.getId()))
                .map(n -> {
                    n.setRead(true);
                    return notificationRepository.save(n);
                });
    }

    @Override
    public long getUnreadCount(User user) {
        return notificationRepository.countByUserAndRead(user, false);
    }
}
