package com.eventkita.service;

import com.eventkita.dto.EventResponseDTO;
import com.eventkita.dto.EventSummaryDTO;
import com.eventkita.dto.TicketTypeRequestDTO;
import com.eventkita.entity.Event;
import com.eventkita.entity.Organizer;
import com.eventkita.entity.User;
import com.eventkita.enums.EventCategory;

import java.util.List;
import java.util.Optional;

public interface EventService {

    // CRUD
    Event createEvent(Event event, Organizer organizer, List<TicketTypeRequestDTO> ticketTypes);
    Event updateEvent(Long id, Event updatedEvent, Organizer organizer, List<TicketTypeRequestDTO> ticketTypes);
    void deleteEvent(Long id, Organizer organizer);
    Optional<Event> getEventEntityById(Long id); // entity murni

    // DTO Responses
    List<EventResponseDTO> getAllEvents();
    EventResponseDTO getEventById(Long id);
    List<EventResponseDTO> getEventsByOrganizer(Long organizerId);

    // Event summary untuk organizer
    List<EventSummaryDTO> findMyEvents(User organizer, String status);

    // Business logic tambahan
    boolean isEventOwnedByOrganizer(Long eventId, Long organizerId);

    List<Event> findPublishedEvents(EventCategory category, String date, String location);
    
}
