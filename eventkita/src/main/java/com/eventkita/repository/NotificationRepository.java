package com.eventkita.repository;

import com.eventkita.entity.Notification;
import com.eventkita.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserOrderByCreatedAtDesc(User user);
    long countByUserAndRead(User user, boolean read);
}
