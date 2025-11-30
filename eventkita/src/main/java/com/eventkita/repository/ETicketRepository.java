package com.eventkita.repository;

import com.eventkita.entity.ETicket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ETicketRepository extends JpaRepository<ETicket, Long> {
    Optional<ETicket> findByTicketCode(String ticketCode);
    Optional<ETicket> findByBooking_Id(Long bookingId);
}