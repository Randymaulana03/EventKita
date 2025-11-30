package com.eventkita.service.impl;

import com.eventkita.entity.ETicket;
import com.eventkita.entity.Booking;
import com.eventkita.service.EmailService;
import com.eventkita.util.EmailUtils;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    private final EmailUtils emailUtils;

    public EmailServiceImpl(EmailUtils emailUtils) {
        this.emailUtils = emailUtils;
    }

    @Override
    public void sendETicketEmail(String to, ETicket eTicket, Booking booking) {
        String subject = "[EventKita] E-Ticket for " + eTicket.getEventName();
        StringBuilder message = new StringBuilder();
        message.append("Halo ").append(eTicket.getAttendeeName()).append(",\n\n");
        message.append("Terima kasih telah membeli tiket. Berikut e-ticket Anda:\n\n");
        message.append("Event: ").append(eTicket.getEventName()).append("\n");
        message.append("Kode E-Ticket: ").append(eTicket.getTicketCode()).append("\n");
        message.append("Booking ID: ").append(booking.getId()).append("\n");
        message.append("Jumlah tiket: ").append(booking.getQuantity()).append("\n\n");
        message.append("Silakan tunjukkan kode ini di pintu masuk acara. Terima kasih.\n\n");
        message.append("- EventKita");

        emailUtils.sendEmail(to, subject, message.toString());
    }
}
