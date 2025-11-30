package com.eventkita.listener;

import com.eventkita.entity.Booking;
import com.eventkita.entity.Payment;
import com.eventkita.event.PaymentSuccessEvent;
import com.eventkita.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class PaymentNotificationListener {

    @Autowired
    private NotificationService notificationService;

    @EventListener
    public void handlePaymentSuccess(PaymentSuccessEvent event) {
        Payment payment = event.getPayment();
        try {
            if (payment != null && payment.getBooking() != null && payment.getBooking().getUser() != null) {
                Booking booking = payment.getBooking();
                String paymentCode = payment.getPaymentCode();
                if(booking.getEvent() == null) {
                    throw new RuntimeException("Event pada booking tidak ditemukan");
                   
                }
                String eventTitle = booking.getEvent().getTitle();

                String title = "Pembayaran Berhasil";
                String message = String.format("Pembayaran untuk %s berhasil. Kode: %s", eventTitle, paymentCode);
                notificationService.createNotification(booking.getUser(), title, message);
            }
        } catch (Exception ex) {
            System.err.println("PaymentNotificationListener failed: " + ex.getMessage());
        }
    }
}
