package com.eventkita.service.impl;

import com.eventkita.entity.Ticket;
import com.eventkita.repository.TicketRepository;
import com.eventkita.service.TicketService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TicketServiceImpl implements TicketService {

    private final TicketRepository repository;

    public TicketServiceImpl(TicketRepository repository) {
        this.repository = repository;
    }

    @Override
    public Ticket createTicket(Ticket ticket) {
        // Simple validation
        if (ticket.getPrice() <= 0) {
            throw new RuntimeException("Ticket price must be positive");
        }
        if (ticket.getQuota() <= 0) {
            throw new RuntimeException("Ticket quota must be positive");
        }
        
        return repository.save(ticket);
    }
    @Override
    public void deleteTicket(Long ticketId) {
        Ticket ticket = repository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));
        repository.delete(ticket);
    }

    @Override
    public List<Ticket> getTicketsByEvent(Long eventId) {
        return repository.findByEvent_Id(eventId);
    }

    @Override
    public Optional<Ticket> findById(Long ticketId) {
        return repository.findById(ticketId);
    }

    @Override
    public Optional<Ticket> getTicketById(Long ticketId) {
        return repository.findById(ticketId);
    }

}