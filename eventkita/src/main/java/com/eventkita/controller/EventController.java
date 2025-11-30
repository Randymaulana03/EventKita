package com.eventkita.controller;

import com.eventkita.dto.EventRequestDTO;
import com.eventkita.dto.EventResponseDTO;
import com.eventkita.dto.EventSummaryDTO;
import com.eventkita.dto.TicketTypeResponseDTO;
import com.eventkita.entity.Event;
import com.eventkita.entity.EventTicketType;
import com.eventkita.entity.Organizer;
import com.eventkita.entity.User;
import com.eventkita.enums.EventCategory;
import com.eventkita.repository.EventTicketTypeRepository;
import com.eventkita.service.EventService;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;




import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.HashMap;

@RestController
@RequestMapping("/api/events")
@CrossOrigin(origins = "*")
public class EventController {

    private final EventService eventService;
    private final EventTicketTypeRepository eventTicketTypeRepository;

    public EventController(EventService eventService, EventTicketTypeRepository eventTicketTypeRepository) {
        this.eventService = eventService;
        this.eventTicketTypeRepository = eventTicketTypeRepository;
    }

    // ===========================
    // CREATE EVENT
    // ===========================
    @PostMapping
    public ResponseEntity<?> createEvent(@RequestBody EventRequestDTO dto, Authentication authentication) {
        try {
            // Get user from authentication principal (User object, not just name)
            Object principal = authentication.getPrincipal();
            if (!(principal instanceof User)) {
                return ResponseEntity.status(403).body("Authentication required");
            }
            
            User user = (User) principal;

            if (!(user instanceof Organizer)) {
                return ResponseEntity.status(403).body("Only Organizers can create events");
            }

            Organizer organizer = (Organizer) user;

            Event event = new Event();
            event.setTitle(dto.getTitle());
            event.setDescription(dto.getDescription());
            event.setStartDate(dto.getStartDate());
            event.setEndDate(dto.getEndDate());
            event.setLocation(dto.getLocation());
            event.setCategory(dto.getCategory());
            event.setStatus(dto.getStatus());
            event.setPrice(dto.getPrice());
            event.setMaxParticipants(dto.getMaxParticipants());
            event.setTags(dto.getTags());
            event.setContactInfo(dto.getContactInfo());

            Event createdEvent = eventService.createEvent(event, organizer, dto.getTicketTypes());

            // convert ke DTO
            EventResponseDTO responseDTO = new EventResponseDTO(createdEvent, 0, 0, 0); // new event has 0 participants
            return ResponseEntity.ok(responseDTO);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error creating event: " + e.getMessage());
        }
    }

    // ===========================
    // GET MY EVENTS (Organizer)
    // ===========================
    @GetMapping("/my-events")
    public ResponseEntity<?> getMyEvents(@AuthenticationPrincipal User organizer,
                                         @RequestParam(required = false) String status) {
        try {
            if (organizer == null) {
                return ResponseEntity.status(403).body("User not authenticated");
            }

            List<EventSummaryDTO> events = eventService.findMyEvents(organizer, status);
            return ResponseEntity.ok(events);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error getting events: " + e.getMessage());
        }
    }

    // ===========================
    // GET EVENT BY ID (Public)
    // ===========================
    @GetMapping("/{id}")
    public ResponseEntity<?> getEventById(@PathVariable Long id) {
        try {
            EventResponseDTO eventDTO = eventService.getEventById(id);
            return ResponseEntity.ok(eventDTO);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Event not found");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(404).body(errorResponse);
        }
    }
    @GetMapping()
    public ResponseEntity<?> listEvents(
            @RequestParam(required = false) EventCategory category,
            @RequestParam(required = false) String date,
            @RequestParam(required = false) String location
    ) {
        List<Event> events;
        if (category != null || date != null || location != null) {
            events = eventService.findPublishedEvents(category, date, location);
        } else {
            events = eventService.getAllEvents()
                    .stream()
                    .map(dto -> dto.toEntity()) // atau konversi ke Event
                    .collect(Collectors.toList());
        }
        return ResponseEntity.ok(events);
    }

   

    // ===========================
    // UPDATE EVENT (Organizer)
    // ===========================
    @PutMapping("/{id}")
    public ResponseEntity<?> updateEvent(@PathVariable Long id, 
                                        @RequestBody EventRequestDTO dto,
                                        Authentication authentication) {
        try {
            Object principal = authentication.getPrincipal();
            if (!(principal instanceof User)) {
                return ResponseEntity.status(403).body("Authentication required");
            }
            
            User user = (User) principal;
            if (!(user instanceof Organizer)) {
                return ResponseEntity.status(403).body("Only Organizers can update events");
            }

            Organizer organizer = (Organizer) user;

            Event event = new Event();
            event.setTitle(dto.getTitle());
            event.setDescription(dto.getDescription());
            event.setStartDate(dto.getStartDate());
            event.setEndDate(dto.getEndDate());
            event.setLocation(dto.getLocation());
            event.setCategory(dto.getCategory());
            event.setStatus(dto.getStatus());
            event.setPrice(dto.getPrice());
            event.setMaxParticipants(dto.getMaxParticipants());
            event.setTags(dto.getTags());
            event.setContactInfo(dto.getContactInfo());

            Event updatedEvent = eventService.updateEvent(id, event, organizer, dto.getTicketTypes());

            // convert ke DTO with proper calculations
            double komisi = updatedEvent.getBookings().stream()
                .filter(b -> b.getStatus() == com.eventkita.enums.BookingStatus.PAID)
                .mapToDouble(b -> b.getTotalAmount()).sum() * 0.1;
            double revenue = updatedEvent.getBookings().stream()
                .filter(b -> b.getStatus() == com.eventkita.enums.BookingStatus.PAID)
                .mapToDouble(b -> b.getTotalAmount()).sum() - komisi;
            int participants = updatedEvent.getBookings().stream()
                .filter(b -> b.getStatus() == com.eventkita.enums.BookingStatus.PAID)
                .mapToInt(b -> b.getQuantity()).sum();
            EventResponseDTO responseDTO = new EventResponseDTO(updatedEvent, komisi, revenue, participants);
            return ResponseEntity.ok(responseDTO);

        } catch (Exception e) {
            e.printStackTrace();
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Error updating event");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
    
    // ===========================
    // GET AVAILABLE TICKET TYPES FOR AN EVENT
    // ===========================
    @GetMapping("/{id}/ticket-types")
    public ResponseEntity<?> getEventTicketTypes(@PathVariable Long id) {
        try {
            List<EventTicketType> ticketTypes = eventTicketTypeRepository.findByEventId(id);
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
