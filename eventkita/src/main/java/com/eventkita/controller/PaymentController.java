package com.eventkita.controller;

import com.eventkita.entity.Booking;
import com.eventkita.entity.Payment;
import com.eventkita.entity.User;
import com.eventkita.enums.PaymentMethod;
import com.eventkita.factory.PaymentFactory;
import com.eventkita.service.BookingService;
import com.eventkita.service.PaymentService;
import com.eventkita.dto.PaymentDTO;
import com.eventkita.dto.PaymentRequest;
import com.eventkita.dto.ProcessPaymentRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.context.ApplicationEventPublisher;
import com.eventkita.event.PaymentSuccessEvent;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin(origins = "*")
public class PaymentController {

    private final PaymentService service;
    private final BookingService bookingService;
    private final ApplicationEventPublisher eventPublisher;

    public PaymentController(PaymentService service, BookingService bookingService, ApplicationEventPublisher eventPublisher) {
        this.service = service;
        this.bookingService = bookingService;
        this.eventPublisher = eventPublisher;
    }

    // ===========================
    // CREATE PAYMENT
    // ===========================
    @PostMapping
    public ResponseEntity<?> createPayment(@RequestBody PaymentRequest request, Authentication authentication) {
        try {
            Object principal = authentication.getPrincipal();
            if (!(principal instanceof User)) {
                return ResponseEntity.status(403).body("Authentication required");
            }

            // Get booking
            Booking booking = bookingService.getBookingById(request.getBookingId())
                    .orElseThrow(() -> new RuntimeException("Booking not found"));

            // Create payment using factory pattern
            PaymentMethod paymentMethod = PaymentMethod.valueOf(request.getPaymentMethod().toUpperCase());
            Payment payment = PaymentFactory.createPayment(booking, paymentMethod, booking.getTotalAmount());

            Payment created = service.createPayment(payment);
            return ResponseEntity.ok(new PaymentDTO(created));
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, String> error = new HashMap<>();
            error.put("error", "Gagal membuat payment");
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    // ===========================
    // GET PAYMENT BY ID
    // ===========================
    @GetMapping("/{id}")
    public ResponseEntity<?> getPayment(@PathVariable Long id) {
        try {
            Payment payment = service.getPaymentById(id)
                    .orElseThrow(() -> new RuntimeException("Payment not found"));
            return ResponseEntity.ok(new PaymentDTO(payment));
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Payment tidak ditemukan");
            return ResponseEntity.status(404).body(error);
        }
    }

    // ===========================
    // GET PAYMENT BY BOOKING
    // ===========================
    @GetMapping("/booking/{bookingId}")
    public ResponseEntity<?> getPaymentByBooking(@PathVariable Long bookingId) {
        try {
            Payment payment = service.getPaymentByBooking(bookingId)
                    .orElseThrow(() -> new RuntimeException("Payment not found"));
            return ResponseEntity.ok(payment);
        } catch (Exception e) {
            return ResponseEntity.status(404).body("Payment tidak ditemukan");
        }
    }

    // ===========================
    // PROCESS PAYMENT (Simulate payment success)
    // ===========================
    @PostMapping("/{id}/process")
    public ResponseEntity<?> processPayment(@PathVariable Long id, @RequestBody ProcessPaymentRequest request, Authentication authentication) {
        try {
            Object principal = authentication.getPrincipal();
            if (!(principal instanceof User)) {
                return ResponseEntity.status(403).body("Authentication required");
            }

            Payment payment = service.getPaymentById(id)
                    .orElseThrow(() -> new RuntimeException("Payment not found"));

            // Update payment method if provided from frontend
            if (request != null && request.getPaymentMethod() != null && !request.getPaymentMethod().isEmpty()) {
                try {
                    PaymentMethod method = PaymentMethod.valueOf(request.getPaymentMethod().toUpperCase());
                    payment.setPaymentMethod(method);
                    System.out.println("DEBUG: Payment method set to: " + method);
                } catch (IllegalArgumentException ex) {
                    System.out.println("DEBUG: Invalid payment method: " + request.getPaymentMethod());
                }
            }

            // Set payment code if provided (from frontend QRIS generation)
            if (request != null && request.getPaymentCode() != null && !request.getPaymentCode().isEmpty()) {
                payment.setPaymentCode(request.getPaymentCode());
                System.out.println("DEBUG: Setting payment code: " + request.getPaymentCode());
            }

            // Process payment (simulate success)
            // This will auto-generate QRIS code if method is QRIS and code is not set
            Payment processed = service.processPayment(payment);
            System.out.println("DEBUG: Payment processed. Code: " + processed.getPaymentCode() + ", Method: " + processed.getPaymentMethod());

            // Publish payment success event (listener will handle booking update, e-ticket, email, komisi)
            eventPublisher.publishEvent(new PaymentSuccessEvent(this, processed));

            return ResponseEntity.ok(Map.of(
                    "message", "Payment berhasil diproses",
                    "payment", new PaymentDTO(processed),
                    "paymentCode", processed.getPaymentCode()
            ));
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, String> error = new HashMap<>();
            error.put("error", "Gagal proses payment");
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    // ===========================
    // CANCEL PAYMENT
    // ===========================
    @PostMapping("/{id}/cancel")
    public ResponseEntity<?> cancelPayment(@PathVariable Long id, Authentication authentication) {
        try {
            Object principal = authentication.getPrincipal();
            if (!(principal instanceof User)) {
                return ResponseEntity.status(403).body("Authentication required");
            }

            service.cancelPayment(id);
            return ResponseEntity.ok(Map.of("message", "Payment berhasil dibatalkan"));
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Gagal batalkan payment");
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
}