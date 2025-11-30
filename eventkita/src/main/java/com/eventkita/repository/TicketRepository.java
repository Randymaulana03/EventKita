package com.eventkita.repository;

import com.eventkita.entity.Event;
import com.eventkita.entity.Ticket;
import com.eventkita.enums.TicketType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByEvent_Id(Long eventId);
    List<Ticket> findByEvent_IdAndType(Long eventId, String type);
    Optional<Ticket> findByEventAndType(Event event, TicketType type);
}