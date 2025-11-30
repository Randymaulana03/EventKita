package com.eventkita.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.eventkita.entity.Booking;
import com.eventkita.entity.Ticket;
import com.eventkita.repository.BookingRepository;
import com.eventkita.repository.TicketRepository;
import java.time.LocalDateTime;
import java.util.List;
import com.eventkita.enums.BookingStatus;

@Component
public class TicketScheduler {

    private final BookingRepository bookingRepository;
    private final TicketRepository ticketRepository;

    public TicketScheduler(BookingRepository bookingRepository, TicketRepository ticketRepository) {
        this.bookingRepository = bookingRepository;
        this.ticketRepository = ticketRepository;
    }

    @Scheduled(fixedRate = 60000) // cek tiap 1 menit
    public void autoCancelPendingTickets() {
        List<Booking> expiredBookings = bookingRepository.findAll().stream()
            .filter(b -> b.getStatus() == BookingStatus.PENDING)
            .filter(b -> b.getExpireTime().isBefore(LocalDateTime.now()))
            .toList();

        for (Booking booking : expiredBookings) {
            booking.setStatus(BookingStatus.CANCELLED);
            // kembalikan kuota
            Ticket ticket = booking.getTicket();
            ticket.setQuota(ticket.getQuota() + booking.getQuantity());
            ticketRepository.save(ticket);
            bookingRepository.save(booking);
            // bisa juga trigger observer untuk notif email
        }
    }
}
