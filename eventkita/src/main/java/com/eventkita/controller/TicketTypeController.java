package com.eventkita.controller;

import com.eventkita.dto.TicketTypeResponseDTO;
import com.eventkita.entity.EventTicketType;
import com.eventkita.repository.EventTicketTypeRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/ticket-types")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5500", "http://localhost:8000"})
public class TicketTypeController {

    private final EventTicketTypeRepository eventTicketTypeRepository;

    public TicketTypeController(EventTicketTypeRepository eventTicketTypeRepository) {
        this.eventTicketTypeRepository = eventTicketTypeRepository;
    }

    // GET /api/ticket-types?eventId=6
    @GetMapping
    public ResponseEntity<?> getTicketTypesByEvent(@RequestParam Long eventId) {
        try {
            List<EventTicketType> ticketTypes = eventTicketTypeRepository.findByEventId(eventId);
            List<TicketTypeResponseDTO> response = ticketTypes.stream()
                .map(TicketTypeResponseDTO::new)
                .collect(Collectors.toList());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error fetching ticket types: " + e.getMessage());
        }
    }
}
