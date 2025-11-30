package com.eventkita.controller;

import com.eventkita.entity.Booking;
import com.eventkita.service.QRCodeService;
import com.eventkita.service.BookingService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/qr")
@CrossOrigin(origins = "*")
public class QRCodeController {

    private final QRCodeService qrService;
    private final BookingService bookingService;

    public QRCodeController(QRCodeService qrService, BookingService bookingService) {
        this.qrService = qrService;
        this.bookingService = bookingService;
    }

    // ===========================
    // GENERATE QR CODE (setelah payment sukses)
    // ===========================
    @PostMapping("/generate/{bookingId}")
    public ResponseEntity<?> generateQRCode(@PathVariable Long bookingId, Authentication authentication) {
        try {
            if (authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.status(403).body("Authentication required");
            }

            String qrCode = qrService.generateQRCode(bookingId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "QR code berhasil generate");
            response.put("qrCode", qrCode);
            response.put("bookingId", bookingId);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, String> error = new HashMap<>();
            error.put("error", "Gagal generate QR code");
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    // ===========================
    // VALIDATE QR CODE (organizer scan di pintu acara)
    // ===========================
    @PostMapping("/validate")
    public ResponseEntity<?> validateQRCode(@RequestParam String qrCode, Authentication authentication) {
        try {
            if (authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.status(403).body("Authentication required");
            }

            boolean isValid = qrService.validateQRCode(qrCode);

            if (!isValid) {
                return ResponseEntity.badRequest().body(Map.of(
                        "valid", false,
                        "message", "QR code tidak valid atau tidak ditemukan"
                ));
            }

            // Get booking details
            Booking booking = bookingService.getBookingById(findBookingIdByQR(qrCode))
                    .orElseThrow(() -> new RuntimeException("Booking not found"));

            Map<String, Object> response = new HashMap<>();
            response.put("valid", true);
            response.put("message", "QR code valid!");
            response.put("bookingId", booking.getId());
            response.put("peserta", booking.getUser().getFullName());
            response.put("quantity", booking.getQuantity());
            response.put("status", booking.getStatus());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error validating QR code");
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    // Helper method
    private Long findBookingIdByQR(String qrCode) {
        // This would need proper implementation with repository call
        return 1L; // placeholder
    }
}
