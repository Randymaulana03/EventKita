package com.eventkita.repository;

import com.eventkita.entity.Komisi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KomisiRepository extends JpaRepository<Komisi, Long> {
    List<Komisi> findByEvent_Id(Long eventId);
    List<Komisi> findByBooking_Id(Long bookingId);
    // Komisi for events created by an organizer: Komisi.event.createdBy.id
    List<Komisi> findByEvent_CreatedBy_Id(Long organizerId);
}