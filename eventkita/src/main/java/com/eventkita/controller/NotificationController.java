package com.eventkita.controller;

import com.eventkita.entity.Notification;
import com.eventkita.entity.User;
import com.eventkita.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
@CrossOrigin(origins = "*")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping
    public ResponseEntity<?> getMyNotifications(Authentication authentication) {
        try {
            Object principal = authentication.getPrincipal();
            if (!(principal instanceof User)) {
                System.err.println("NotificationController: principal is not User instance");
                return ResponseEntity.status(403).body(Map.of("error", "Authentication required"));
            }

            User user = (User) principal;
            System.out.println("NotificationController: Getting notifications for user " + user.getId());
            List<Notification> list = notificationService.getUserNotifications(user);
            System.out.println("NotificationController: Found " + list.size() + " notifications");
            
            // Convert to simple DTOs to avoid lazy loading issues
            List<Map<String, Object>> dtoList = list.stream().map(n -> {
                Map<String, Object> dto = new java.util.HashMap<>();
                dto.put("id", n.getId());
                dto.put("title", n.getTitle());
                dto.put("message", n.getMessage());
                dto.put("read", n.isRead());
                dto.put("createdAt", n.getCreatedAt());
                return dto;
            }).toList();
            
            return ResponseEntity.ok(dtoList);
        } catch (Exception e) {
            System.err.println("NotificationController ERROR: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of("error", "Gagal memuat notifikasi: " + e.getMessage()));
        }
    }

    @GetMapping("/unread-count")
    public ResponseEntity<?> getUnreadCount(Authentication authentication) {
        try {
            Object principal = authentication.getPrincipal();
            if (!(principal instanceof User)) {
                return ResponseEntity.status(403).body(Map.of("error", "Authentication required"));
            }

            User user = (User) principal;
            long count = notificationService.getUnreadCount(user);
            return ResponseEntity.ok(Map.of("count", count));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of("error", "Gagal memuat unread count"));
        }
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<?> markRead(@PathVariable Long id, Authentication authentication) {
        try {
            Object principal = authentication.getPrincipal();
            if (!(principal instanceof User)) {
                return ResponseEntity.status(403).body(Map.of("error", "Authentication required"));
            }
            User user = (User) principal;
            var opt = notificationService.markAsRead(id, user);
            if (opt.isEmpty()) {
                return ResponseEntity.status(404).body(Map.of("error", "Notification not found or unauthorized"));
            }
            return ResponseEntity.ok(opt.get());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of("error", "Gagal mengubah status notifikasi"));
        }
    }
}
