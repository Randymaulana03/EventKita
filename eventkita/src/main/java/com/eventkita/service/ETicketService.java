package com.eventkita.service;

import com.eventkita.entity.ETicket;
import com.eventkita.entity.Booking;

public interface ETicketService {
    ETicket generateETicket(Booking booking, String qrCode);
    ETicket getByBookingId(Long bookingId);
}
