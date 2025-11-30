package com.eventkita.service;

import com.eventkita.entity.Payment;

import java.util.Optional;

public interface PaymentService {

    Payment createPayment(Payment payment);

    Optional<Payment> getPaymentById(Long id);

    Optional<Payment> getPaymentByBooking(Long bookingId);

    Payment processPayment(Payment payment);

    void cancelPayment(Long paymentId);
}
