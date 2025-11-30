package com.eventkita.listener;

import com.eventkita.entity.Booking;
import com.eventkita.entity.Komisi;
import com.eventkita.entity.Payment;
import com.eventkita.enums.BookingStatus;
import com.eventkita.event.PaymentSuccessEvent;
import com.eventkita.repository.BookingRepository;
import com.eventkita.repository.KomisiRepository;
import com.eventkita.entity.ETicket;
import com.eventkita.service.ETicketService;
import com.eventkita.service.EmailService;
import com.eventkita.service.QRCodeService;
import com.eventkita.service.KomisiService;
// Komisi strategy implementations are available under com.eventkita.strategy.komisi
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class PaymentSuccessListener {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private QRCodeService qrCodeService;

    @Autowired
    private KomisiService komisiService;

    @Autowired
    private KomisiRepository komisiRepository;
    
    @Autowired
    private ETicketService eTicketService;

    @Autowired
    private EmailService emailService;

    @EventListener
    public void handlePaymentSuccess(PaymentSuccessEvent event) {
        Payment payment = event.getPayment();
        
        try {
            // 1. Update booking status to PAID
            if (payment.getBooking() != null) {
                Booking booking = payment.getBooking();
                booking.setStatus(BookingStatus.PAID);
                booking.setPaidAt(LocalDateTime.now());
                bookingRepository.save(booking);

                // 2. Set QR code from payment code (payment code IS the QR code)
                String paymentCode = payment.getPaymentCode();
                booking.setQrCode(paymentCode);  // Save payment code as QR code
                booking.setKodeBooking(paymentCode);  // Also save as kode_booking for reference
                bookingRepository.save(booking);
                System.out.println("DEBUG: Payment code saved as QR code: " + paymentCode);

                // 3. Generate ETicket record (use payment code as QR)
                ETicket eTicket = eTicketService.generateETicket(booking, paymentCode);

                // 4. Send email with e-ticket
                if (booking.getUser() != null && booking.getUser().getEmail() != null) {
                    try {
                        emailService.sendETicketEmail(booking.getUser().getEmail(), eTicket, booking);
                    } catch (Exception ex) {
                        System.err.println("Failed to send e-ticket email: " + ex.getMessage());
                    }
                }

                // notification creation moved to a dedicated observer (PaymentNotificationListener)

                // 4. TODO: Update event kuota/inventory
                // eventService.updateQuota(booking.getEvent().getId(), booking.getQuantity());

                // 5. Record komisi
                if (booking.getEvent() != null && booking.getEvent().getCreatedBy() != null) {
                    double totalAmount = booking.getTotalAmount() != null ? booking.getTotalAmount() : 0.0;

                    // Use KomisiService to calculate komisi (uses default or event-based percentage)
                    double komisiAmount = komisiService.calculateKomisi(booking.getEvent().getId(), totalAmount);

                    Komisi komisi = new Komisi();
                    // set percentage to default 10.0 (KomisiService.calculateKomisi uses same default when appropriate)
                    komisi.setPersentase(10.0);
                    komisi.setAmount(komisiAmount);
                    komisi.setBooking(booking);
                    komisi.setEvent(booking.getEvent());

                    komisiRepository.save(komisi);
                }
            }
        } catch (Exception e) {
            // Log error but don't fail the payment process
            System.err.println("Error processing payment success event: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
