package com.eventkita.service.impl;

import com.eventkita.entity.Payment;
import com.eventkita.enums.PaymentStatus;
import com.eventkita.repository.PaymentRepository;
import com.eventkita.service.PaymentService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository repository;

    public PaymentServiceImpl(PaymentRepository repository) {
        this.repository = repository;
    }

    // Generate random 20-character payment code for QRIS
    private String generatePaymentCode() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 20).toUpperCase();
    }

    @Override
    public Payment createPayment(Payment payment) {
        if (payment.getStatus() == null) {
            payment.setStatus(PaymentStatus.PENDING);
        }
        if (payment.getCreatedAt() == null) {
            payment.setCreatedAt(LocalDateTime.now());
        }

        return repository.save(payment);
    }

    @Override
    public Optional<Payment> getPaymentById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Optional<Payment> getPaymentByBooking(Long bookingId) {
        return repository.findByBooking_Id(bookingId);
    }

    @Override
    public Payment processPayment(Payment payment) {
        if (!payment.canBeProcessed()) {
            throw new RuntimeException("Payment cannot be processed. Current status: " + payment.getStatus());
        }

        // Auto-generate payment code if not already set (for all payment methods)
        if (payment.getPaymentCode() == null || payment.getPaymentCode().isEmpty()) {
            payment.setPaymentCode(generatePaymentCode());
            System.out.println("DEBUG: Generated payment code: " + payment.getPaymentCode());
        }

        payment.markAsSuccess();
        return repository.save(payment);
    }

    @Override
    public void cancelPayment(Long paymentId) {
        Payment payment = repository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        if (!payment.getStatus().equals(PaymentStatus.PENDING)) {
            throw new RuntimeException("Hanya payment dengan status PENDING yang bisa dibatalkan");
        }

        payment.setStatus(PaymentStatus.CANCELLED);
        repository.save(payment);
    }
}
