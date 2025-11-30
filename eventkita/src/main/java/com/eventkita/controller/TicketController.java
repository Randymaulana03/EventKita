package com.eventkita.controller;

import com.eventkita.dto.TicketDTO;
import com.eventkita.entity.Event;
import com.eventkita.entity.Ticket;
import com.eventkita.enums.TicketType;
import com.eventkita.factory.TicketFactory;
import com.eventkita.repository.EventRepository;
import com.eventkita.service.TicketService;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    private final TicketService service;
    private final EventRepository eventRepository;

    public TicketController(TicketService service, EventRepository eventRepository) {
        this.service = service;
        this.eventRepository = eventRepository;
    }

    // ================= CREATE =================
    @PostMapping
    public Ticket createTicket(@RequestBody TicketDTO request) {
        Event event = eventRepository.findById(request.getEventId())
                .orElseThrow(() -> new RuntimeException("Event not found"));

        TicketType type;
        try {
            type = TicketType.valueOf(request.getType().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid ticket type");
        }

        Ticket ticket = TicketFactory.createTicket(
                event,
                type,
                request.getPrice(),
                request.getQuota()
        );

        return service.createTicket(ticket);
    }

    // ================= READ =================
    @GetMapping("/event/{eventId}")
    public List<Ticket> getTicketsByEvent(@PathVariable Long eventId) {
        return service.getTicketsByEvent(eventId);
    }

    @GetMapping("/{id}")
    public Ticket getTicketById(@PathVariable Long id) {
        return service.getTicketById(id)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));
    }

    // ================= UPDATE =================
    @PutMapping("/{id}")
    public Ticket updateTicket(@PathVariable Long id, @RequestBody TicketDTO request) {
        Ticket ticket = service.getTicketById(id)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        if (request.getPrice() != null) ticket.setPrice(request.getPrice());
        if (request.getQuota() != null) ticket.setQuota(request.getQuota());
        if (request.getType() != null) {
            try {
                ticket.setType(TicketType.valueOf(request.getType().toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Invalid ticket type");
            }
        }

        return service.createTicket(ticket); // save update
    }

    // ================= DELETE =================
    @DeleteMapping("/{id}")
    public void deleteTicket(@PathVariable Long id) {
        service.deleteTicket(id);
    }

    // ================= UTILITY: Auto-create default tickets =================
    @PostMapping("/{eventId}/auto-create-defaults")
    public ResponseEntity<?> autoCreateDefaultTickets(@PathVariable Long eventId) {
        try {
            Event event = eventRepository.findById(eventId)
                    .orElseThrow(() -> new RuntimeException("Event not found"));

            // Check if event already has tickets
            List<Ticket> existing = service.getTicketsByEvent(eventId);
            if (existing != null && !existing.isEmpty()) {
                return ResponseEntity.ok(Map.of("message", "Event sudah memiliki tikets", "count", existing.size()));
            }

            // Create default tickets: REGULAR, VIP, VVIP
            Ticket regular = TicketFactory.createTicket(event, TicketType.REGULAR, 100000.0, 100);
            Ticket vip = TicketFactory.createTicket(event, TicketType.VIP, 250000.0, 50);
            Ticket vvip = TicketFactory.createTicket(event, TicketType.VVIP, 500000.0, 20);

            service.createTicket(regular);
            service.createTicket(vip);
            service.createTicket(vvip);

            return ResponseEntity.ok(Map.of(
                    "message", "Tikets default berhasil dibuat",
                    "count", 3,
                    "types", new String[]{"REGULAR", "VIP", "VVIP"}
            ));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of("error", "Gagal membuat tikets default", "message", e.getMessage()));
        }
    }
}
