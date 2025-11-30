package com.eventkita.service.impl;

import com.eventkita.dto.EventResponseDTO;
import com.eventkita.dto.EventSummaryDTO;
import com.eventkita.dto.TicketTypeRequestDTO;
import com.eventkita.entity.Event;
import com.eventkita.entity.EventTicketType;
import com.eventkita.entity.Organizer;
import com.eventkita.entity.User;
import com.eventkita.enums.EventStatus;
import com.eventkita.repository.EventRepository;
import com.eventkita.repository.EventTicketTypeRepository;
import com.eventkita.service.EventService;
import com.eventkita.service.UserService;

import com.eventkita.enums.EventCategory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final EventTicketTypeRepository eventTicketTypeRepository;
    private final UserService userService;

    public EventServiceImpl(EventRepository eventRepository, 
                           EventTicketTypeRepository eventTicketTypeRepository,
                           UserService userService) {
        this.eventRepository = eventRepository;
        this.eventTicketTypeRepository = eventTicketTypeRepository;
        this.userService = userService;
    }

    // ===========================
    // CRUD
    // ===========================

    @Override
    public Event createEvent(Event event, Organizer organizer, List<TicketTypeRequestDTO> ticketTypes) {
        event.setCreatedBy(organizer);
        Event savedEvent = eventRepository.save(event);
        
        // Simpan tipe tiket yang dipilih organizer
        if (ticketTypes != null && !ticketTypes.isEmpty()) {
            for (TicketTypeRequestDTO ticketTypeDTO : ticketTypes) {
                EventTicketType eventTicketType = EventTicketType.builder()
                    .event(savedEvent)
                    .ticketType(ticketTypeDTO.getTicketType())
                    .price(ticketTypeDTO.getPrice())
                    .quota(ticketTypeDTO.getQuota())
                    .availableQuota(ticketTypeDTO.getQuota())
                    .build();
                eventTicketTypeRepository.save(eventTicketType);
            }
        }
        
        return savedEvent;
    }

    @Override
    public Event updateEvent(Long id, Event updatedEvent, Organizer organizer, List<TicketTypeRequestDTO> ticketTypes) {
        Event existing = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        if (!isEventOwnedByOrganizer(id, organizer.getId())) {
            throw new RuntimeException("Unauthorized: Organizer can only update their own event");
        }

        existing.setTitle(updatedEvent.getTitle());
        existing.setDescription(updatedEvent.getDescription());
        existing.setStartDate(updatedEvent.getStartDate());
        existing.setEndDate(updatedEvent.getEndDate());
        existing.setLocation(updatedEvent.getLocation());
        existing.setCategory(updatedEvent.getCategory());
        existing.setStatus(updatedEvent.getStatus());
        existing.setPrice(updatedEvent.getPrice());
        existing.setMaxParticipants(updatedEvent.getMaxParticipants());
        existing.setTags(updatedEvent.getTags());
        existing.setContactInfo(updatedEvent.getContactInfo());

        Event savedEvent = eventRepository.save(existing);
        
        // Update ticket types: hapus yang lama, tambah yang baru
        if (ticketTypes != null) {
            // Hapus semua ticket types yang lama
            eventTicketTypeRepository.deleteByEvent(existing);
            
            // Tambah ticket types yang baru
            for (TicketTypeRequestDTO ticketTypeDTO : ticketTypes) {
                EventTicketType eventTicketType = EventTicketType.builder()
                    .event(savedEvent)
                    .ticketType(ticketTypeDTO.getTicketType())
                    .price(ticketTypeDTO.getPrice())
                    .quota(ticketTypeDTO.getQuota())
                    .availableQuota(ticketTypeDTO.getQuota())
                    .build();
                eventTicketTypeRepository.save(eventTicketType);
            }
        }

        return savedEvent;
    }

    @Override
    public void deleteEvent(Long id, Organizer organizer) {
        Event existing = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        if (!isEventOwnedByOrganizer(id, organizer.getId())) {
            throw new RuntimeException("Unauthorized: Organizer can only delete their own event");
        }

        eventRepository.delete(existing);
    }

    @Override
    public Optional<Event> getEventEntityById(Long id) {
        return eventRepository.findById(id);
    }

    // ===========================
    // DTO Responses
    // ===========================
    @Override
    public List<Event> findPublishedEvents(EventCategory category, String date, String location) {
        // delegasi ke repository (lihat repositori di bawah)
        return eventRepository.findByFilters(EventStatus.PUBLISHED, category, date, location);
    }
    @Override
    public List<EventResponseDTO> getAllEvents() {
        return eventRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public EventResponseDTO getEventById(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found"));
        return mapToDTO(event);
    }

    @Override
    public List<EventResponseDTO> getEventsByOrganizer(Long organizerId) {
        return eventRepository.findByCreatedBy_Id(organizerId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // ===========================
    // Helper methods
    // ===========================

    private EventResponseDTO mapToDTO(Event event) {
        double komisi = calculateKomisi(event);
        double revenueBersih = calculateRevenueBersih(event, komisi);
        int totalParticipants = calculateTotalParticipants(event);
        return new EventResponseDTO(event, komisi, revenueBersih, totalParticipants);
    }

    private double calculateKomisi(Event event) {
        // Komisi 10% dari total revenue PAID bookings
        double totalRevenue = event.getBookings().stream()
            .filter(b -> b.getStatus() == com.eventkita.enums.BookingStatus.PAID)
            .mapToDouble(b -> b.getTotalAmount())
            .sum();
        return totalRevenue * 0.1;
    }

    private double calculateRevenueBersih(Event event, double komisi) {
        // Total revenue dari PAID bookings minus komisi
        double totalRevenue = event.getBookings().stream()
            .filter(b -> b.getStatus() == com.eventkita.enums.BookingStatus.PAID)
            .mapToDouble(b -> b.getTotalAmount())
            .sum();
        return totalRevenue - komisi;
    }

    private int calculateTotalParticipants(Event event) {
        // Count total quantity dari PAID bookings
        return event.getBookings().stream()
            .filter(b -> b.getStatus() == com.eventkita.enums.BookingStatus.PAID)
            .mapToInt(b -> b.getQuantity())
            .sum();
    }

    @Override
    public boolean isEventOwnedByOrganizer(Long eventId, Long organizerId) {
        return eventRepository.findById(eventId)
                .map(e -> e.getCreatedBy().getId().equals(organizerId))
                .orElse(false);
    }

    @Override
    public List<EventSummaryDTO> findMyEvents(User organizer, String status) {
        if (!(organizer instanceof Organizer)) {
            throw new RuntimeException("User is not an organizer");
        }

        Long organizerId = organizer.getId();
        List<Event> events;

        if (status != null && !status.isEmpty()) {
            try {
                EventStatus eventStatus = EventStatus.valueOf(status.toUpperCase());
                events = eventRepository.findByCreatedBy_IdAndStatusOrderByStartDateDesc(organizerId, eventStatus);
            } catch (IllegalArgumentException e) {
                events = eventRepository.findByCreatedBy_IdOrderByStartDateDesc(organizerId);
            }
        } else {
            events = eventRepository.findByCreatedBy_IdOrderByStartDateDesc(organizerId);
        }

        return events.stream()
                .map(EventSummaryDTO::new)
                .collect(Collectors.toList());
    }
}
