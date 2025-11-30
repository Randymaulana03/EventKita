package com.eventkita.service.impl;

import com.eventkita.dto.BookingRequestDTO;
import com.eventkita.entity.Booking;
import com.eventkita.entity.Event;
import com.eventkita.entity.EventTicketType;
import com.eventkita.entity.Ticket;
import com.eventkita.entity.User;
import com.eventkita.enums.BookingStatus;
import com.eventkita.factory.TicketFactory;
import com.eventkita.repository.BookingRepository;
import com.eventkita.repository.EventRepository;
import com.eventkita.repository.EventTicketTypeRepository;
import com.eventkita.repository.TicketRepository;
import com.eventkita.service.BookingService;
import com.eventkita.enums.TicketType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


import java.time.LocalDateTime;

@Service
@Transactional
public class BookingServiceImpl implements BookingService {

    private final BookingRepository repository;
    private final EventRepository eventRepository;
    private final EventTicketTypeRepository eventTicketTypeRepository;
    private final TicketRepository ticketRepository;

    public BookingServiceImpl(BookingRepository repository,
                             EventRepository eventRepository,
                             EventTicketTypeRepository eventTicketTypeRepository,
                             TicketRepository ticketRepository) {
        this.repository = repository;
        this.eventRepository = eventRepository;
        this.eventTicketTypeRepository = eventTicketTypeRepository;
        this.ticketRepository = ticketRepository;
    }

    @Override
    public Booking createBookingFromRequest(BookingRequestDTO request, User user) {
        // Validate event
        Event event = eventRepository.findById(request.getEventId())
                .orElseThrow(() -> new RuntimeException("Event not found"));

        // Resolve ticket type enum
        TicketType ticketType;
        try {
            ticketType = TicketType.valueOf(request.getTicketType().toString());
        } catch (Exception ex) {
            throw new RuntimeException("Invalid ticket type: " + request.getTicketType());
        }

        // Find configured EventTicketType
        EventTicketType ett = eventTicketTypeRepository.findByEventIdAndTicketType(event.getId(), ticketType)
                .orElseThrow(() -> new RuntimeException("Ticket type not available for this event"));

        // Check quota
        int qty = request.getQuantity() == null ? 1 : request.getQuantity();
        if (qty <= 0) throw new RuntimeException("Quantity must be >= 1");
        if (ett.getAvailableQuota() < qty) {
            throw new RuntimeException("Not enough quota for selected ticket type");
        }

        // Reduce quota and persist
        boolean reduced = ett.reduceQuota(qty);
        if (!reduced) {
            throw new RuntimeException("Failed to reserve tickets - quota insufficient");
        }
        eventTicketTypeRepository.save(ett);

        // Find or create Ticket entity corresponding to this EventTicketType
        Ticket ticket = ticketRepository.findByEventAndType(event, ticketType)
                .orElseGet(() -> {
                    Ticket t = TicketFactory.createTicketFromEventTicketType(ett);
                    return ticketRepository.save(t);
                });

        // Build booking
        Booking booking = new Booking();
        booking.setEvent(event);
        booking.setTicket(ticket);
        booking.setUser(user);
        booking.setQuantity(qty);
        booking.setTotalAmount(ticket.getPrice() * qty);
        booking.setStatus(BookingStatus.PENDING);
        booking.setCreatedAt(LocalDateTime.now());
        booking.setExpireTime(LocalDateTime.now().plusHours(2));

        return repository.save(booking);
    }

    @Override
    public Booking createBooking(Booking booking) {
        // Set default values
        booking.setStatus(BookingStatus.PENDING);
        booking.setCreatedAt(LocalDateTime.now());
        booking.setExpireTime(LocalDateTime.now().plusHours(2));
        
        // Calculate total amount dari ticket price * quantity
        if (booking.getTicket() != null && booking.getQuantity() != null) {
            booking.setTotalAmount(booking.getTicket().getPrice() * booking.getQuantity());
        }
        
        return repository.save(booking);
    }
    
    

    @Override
    public Optional<Booking> getBookingById(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<Booking> getUserBookings(Long userId) {
        return repository.findByUser_Id(userId);
    }

    @Override
    public List<Booking> getEventBookings(Long eventId) {
        return repository.findByEvent_Id(eventId);
    }

    @Override
    public Optional<Booking> findByQrCode(String qrCode) {
        return repository.findByQrCode(qrCode);
    }

    @Override
    public Booking updateBookingStatus(Long bookingId, BookingStatus status) {
        Booking booking = repository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        
        booking.setStatus(status);
        
        // Jika status PAID, set paidAt timestamp
        if (status == BookingStatus.PAID) {
            booking.setPaidAt(LocalDateTime.now());
        }
        
        return repository.save(booking);
    }

    @Override
    public void cancelBooking(Long bookingId) {
        Booking booking = repository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        
        // Only allow cancel if status is PENDING
        if (!booking.getStatus().equals(BookingStatus.PENDING)) {
            throw new RuntimeException("Hanya booking dengan status PENDING yang bisa dibatalkan");
        }
        
        booking.setStatus(BookingStatus.CANCELLED);
        repository.save(booking);
    }
}