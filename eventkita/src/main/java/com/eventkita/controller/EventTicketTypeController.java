package com.eventkita.controller;

import com.eventkita.dto.TicketTypeResponseDTO;
import com.eventkita.entity.EventTicketType;
import com.eventkita.repository.EventTicketTypeRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller untuk mengelola informasi tipe tiket yang tersedia per event
 */
@RestController
@RequestMapping("/api/events")
@CrossOrigin(origins = "*")
public class EventTicketTypeController {

    private final EventTicketTypeRepository eventTicketTypeRepository;

    public EventTicketTypeController(EventTicketTypeRepository eventTicketTypeRepository) {
        this.eventTicketTypeRepository = eventTicketTypeRepository;
    }

    /**
     * GET /api/events/{eventId}/ticket-types
     * Mengambil semua tipe tiket yang tersedia untuk event tertentu
     */
    @GetMapping("/{eventId}/ticket-types")
    public ResponseEntity<?> getAvailableTicketTypes(@PathVariable Long eventId) {
        try {
            List<EventTicketType> ticketTypes = eventTicketTypeRepository.findByEventId(eventId);
            
            List<TicketTypeResponseDTO> response = ticketTypes.stream()
                    .map(TicketTypeResponseDTO::new)
                    .collect(Collectors.toList());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error fetching ticket types: " + e.getMessage());
        }
    }
}
