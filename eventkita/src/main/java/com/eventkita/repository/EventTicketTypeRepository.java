package com.eventkita.repository;

import com.eventkita.entity.Event;
import com.eventkita.entity.EventTicketType;
import com.eventkita.enums.TicketType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventTicketTypeRepository extends JpaRepository<EventTicketType, Long> {
    
    /**
     * Mengambil semua tipe tiket yang tersedia untuk sebuah event
     */
    List<EventTicketType> findByEvent(Event event);
    
    /**
     * Mengambil semua tipe tiket yang tersedia untuk event ID tertentu
     */
    List<EventTicketType> findByEventId(Long eventId);
    
    /**
     * Mencari tipe tiket spesifik untuk sebuah event
     */
    Optional<EventTicketType> findByEventAndTicketType(Event event, TicketType ticketType);
    
    /**
     * Mencari tipe tiket spesifik berdasarkan event ID dan ticket type
     */
    Optional<EventTicketType> findByEventIdAndTicketType(Long eventId, TicketType ticketType);
    
    /**
     * Menghapus semua tipe tiket untuk sebuah event
     */
    void deleteByEvent(Event event);
    
    /**
     * Cek apakah event memiliki tipe tiket tertentu
     */
    boolean existsByEventAndTicketType(Event event, TicketType ticketType);
}
