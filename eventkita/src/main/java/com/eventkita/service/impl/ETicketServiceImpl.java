package com.eventkita.service.impl;

import com.eventkita.entity.ETicket;
import com.eventkita.entity.Booking;
import com.eventkita.repository.ETicketRepository;
import com.eventkita.service.ETicketService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ETicketServiceImpl implements ETicketService {

    private final ETicketRepository repository;

    public ETicketServiceImpl(ETicketRepository repository) {
        this.repository = repository;
    }

    @Override
    public ETicket generateETicket(Booking booking, String qrCode) {
        ETicket e = ETicket.builder()
                .ticketCode("ET-" + booking.getId() + "-" + UUID.randomUUID().toString().substring(0,8))
                .booking(booking)
                .build();

        return repository.save(e);
    }

    @Override
    public ETicket getByBookingId(Long bookingId) {
        return repository.findByBooking_Id(bookingId).orElse(null);
    }
}
