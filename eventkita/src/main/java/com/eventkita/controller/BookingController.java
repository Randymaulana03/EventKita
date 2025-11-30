package com.eventkita.controller;

import com.eventkita.dto.BookingRequestDTO;
import com.eventkita.dto.BookingResponseDTO;
import com.eventkita.entity.Booking;
import com.eventkita.entity.User;
import com.eventkita.enums.BookingStatus;
import com.eventkita.service.BookingService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bookings")
@CrossOrigin(origins = "*")
public class BookingController {

    private final BookingService service;

    public BookingController(BookingService service) {
        this.service = service;
    }

    // ===========================
    // CREATE BOOKING (NEW: Using BookingRequestDTO)
    // ===========================
    @PostMapping
    public ResponseEntity<?> createBooking(@RequestBody BookingRequestDTO request, Authentication authentication) {
        try {
            Object principal = authentication.getPrincipal();
            if (!(principal instanceof User)) {
                return ResponseEntity.status(403).body("Authentication required");
            }

            User user = (User) principal;
            Booking created = service.createBookingFromRequest(request, user);
            BookingResponseDTO response = new BookingResponseDTO(created);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, String> error = new HashMap<>();
            error.put("error", "Gagal membuat booking");
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    // ===========================
    // GET BOOKING BY ID
    // ===========================
    @GetMapping("/{id}")
    public ResponseEntity<?> getBooking(@PathVariable Long id) {
        try {
            Booking booking = service.getBookingById(id)
                    .orElseThrow(() -> new RuntimeException("Booking not found"));
            return ResponseEntity.ok(booking);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Booking tidak ditemukan");
            return ResponseEntity.status(404).body(error);
        }
    }

    // ===========================
    // GET USER'S BOOKINGS
    // ===========================
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getUserBookings(@PathVariable Long userId) {
        try {
            List<Booking> bookings = service.getUserBookings(userId);
            return ResponseEntity.ok(bookings);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Gagal memuat booking peserta");
        }
    }

    // ===========================
    // GET EVENT'S BOOKINGS (Organizer)
    // ===========================
    @GetMapping("/event/{eventId}")
    public ResponseEntity<?> getEventBookings(@PathVariable Long eventId) {
        try {
            List<Booking> bookings = service.getEventBookings(eventId);
            // Filter hanya PAID bookings dan convert ke DTO
            List<BookingResponseDTO> response = bookings.stream()
                .filter(b -> b.getStatus() == com.eventkita.enums.BookingStatus.PAID)
                .map(BookingResponseDTO::new)
                .collect(java.util.stream.Collectors.toList());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Gagal memuat booking event");
        }
    }

    // ===========================
    // GET BOOKING BY QR CODE (Public/Validator)
    // ===========================
    @GetMapping("/qr/{qrCode}")
    public ResponseEntity<?> getBookingByQr(@PathVariable String qrCode) {
        try {
            var bookingOpt = service.findByQrCode(qrCode);
            if (bookingOpt.isEmpty()) {
                return ResponseEntity.status(404).body(Map.of("error", "Booking not found"));
            }
            // Build a minimal DTO to avoid deep nested serialization / circular references
            Booking b = bookingOpt.get();
            Map<String, Object> dto = new HashMap<>();
            dto.put("id", b.getId());
            dto.put("eventId", b.getEvent() != null ? b.getEvent().getId() : null);
            dto.put("eventTitle", b.getEvent() != null ? b.getEvent().getTitle() : null);
            dto.put("ticketId", b.getTicket() != null ? b.getTicket().getId() : null);
            dto.put("quantity", b.getQuantity());
            dto.put("totalAmount", b.getTotalAmount());
            dto.put("status", b.getStatus() != null ? b.getStatus().name() : null);
            dto.put("qrCode", b.getQrCode());
            dto.put("kodeBooking", b.getKodeBooking());
            dto.put("createdAt", b.getCreatedAt());
            dto.put("paidAt", b.getPaidAt());
            dto.put("userId", b.getUser() != null ? b.getUser().getId() : null);
            dto.put("userFullName", b.getUser() != null ? b.getUser().getFullName() : null);

            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of("error", "Gagal memuat booking"));
        }
    }

    // ===========================
    // UPDATE BOOKING STATUS (Organizer - Approve/Reject)
    // ===========================
    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateBookingStatus(@PathVariable Long id, 
                                                 @RequestParam String status,
                                                 Authentication authentication) {
        try {
            Object principal = authentication.getPrincipal();
            if (!(principal instanceof User)) {
                return ResponseEntity.status(403).body("Authentication required");
            }

            BookingStatus bookingStatus = BookingStatus.valueOf(status.toUpperCase());
            Booking updated = service.updateBookingStatus(id, bookingStatus);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, String> error = new HashMap<>();
            error.put("error", "Gagal update status booking");
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    // ===========================
    // CANCEL BOOKING
    // ===========================
    @DeleteMapping("/{id}")
    public ResponseEntity<?> cancelBooking(@PathVariable Long id, Authentication authentication) {
        try {
            Object principal = authentication.getPrincipal();
            if (!(principal instanceof User)) {
                return ResponseEntity.status(403).body("Authentication required");
            }

            service.cancelBooking(id);
            return ResponseEntity.ok(Map.of("message", "Booking berhasil dibatalkan"));
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, String> error = new HashMap<>();
            error.put("error", "Gagal batalkan booking");
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
}