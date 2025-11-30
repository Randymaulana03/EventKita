package com.eventkita.service;

import com.eventkita.entity.ETicket;
import com.eventkita.entity.Booking;

public interface EmailService {
    void sendETicketEmail(String to, ETicket eTicket, Booking booking);
}
