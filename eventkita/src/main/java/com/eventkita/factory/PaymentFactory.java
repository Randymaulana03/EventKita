package com.eventkita.factory;

import com.eventkita.entity.Booking;
import com.eventkita.entity.Payment;
import com.eventkita.enums.PaymentMethod;
import com.eventkita.enums.PaymentStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public class PaymentFactory {

    public static Payment createPayment(Booking booking, PaymentMethod method, Double amount) {
        Payment payment = new Payment();
        payment.setBooking(booking);
        payment.setPaymentMethod(method);
        payment.setAmount(amount);
        payment.setStatus(PaymentStatus.PENDING);
        payment.setCreatedAt(LocalDateTime.now());
        
        // Generate unique reference ID
        payment.setReferenceId(generateReferenceId());
        
        return payment;
    }

    // Convenience overload: accept payment method as string (e.g. "BANK_TRANSFER", "EWALLET", "QRIS")
    public static Payment createPayment(Booking booking, String methodName, Double amount) {
        PaymentMethod method;
        try {
            method = PaymentMethod.valueOf(methodName.toUpperCase());
        } catch (IllegalArgumentException ex) {
            method = PaymentMethod.BANK_TRANSFER; // default to BANK_TRANSFER
        }

        return createPayment(booking, method, amount);
    }

    private static String generateReferenceId() {
        return "PAY-" + System.currentTimeMillis() + "-" + UUID.randomUUID().toString().substring(0, 8);
    }
}
