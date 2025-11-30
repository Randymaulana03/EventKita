package com.eventkita.service;

import com.eventkita.entity.Ticket;

import java.util.List;
import java.util.Optional;

public interface TicketService {
    Ticket createTicket(Ticket ticket);
    List<Ticket> getTicketsByEvent(Long eventId);
    Optional<Ticket> findById(Long ticketId);
    Optional<Ticket> getTicketById(Long ticketId);
    void deleteTicket(Long ticketId);
}


