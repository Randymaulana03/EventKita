package com.eventkita.repository;

import com.eventkita.entity.Event;
import com.eventkita.enums.EventStatus;
import com.eventkita.enums.EventCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    // ===============================
    // Filter by basic fields
    // ===============================
    
    List<Event> findByStatus(EventStatus status);
    List<Event> findByCategory(EventCategory category);
    List<Event> findByLocationContainingIgnoreCase(String location);
    List<Event> findByTitleContainingIgnoreCase(String keyword);
    List<Event> findByCreatedBy_IdOrderByStartDateDesc(Long createdById);
    List<Event> findByCreatedBy_IdAndStatusOrderByStartDateDesc(Long createdById, EventStatus status);

    // ===============================
    // Organizer-specific queries
    // ===============================
    
    // Semua event milik organizer tertentu
    List<Event> findByCreatedBy_Id(Long organizerId);
    
    // Semua event milik organizer tertentu dengan status tertentu
    List<Event> findByCreatedBy_IdAndStatus(Long organizerId, EventStatus status);

    // Cari event organizer dengan kata kunci di title
    List<Event> findByCreatedBy_IdAndTitleContainingIgnoreCase(Long organizerId, String keyword);

    // Cari event organizer berdasarkan lokasi
    List<Event> findByCreatedBy_IdAndLocationContainingIgnoreCase(Long organizerId, String location);


    // ===============================
    // Upcoming events
    // ===============================
    
    @Query("SELECT e FROM Event e WHERE e.startDate > :now AND e.status = com.eventkita.enums.EventStatus.PUBLISHED")
    List<Event> findUpcomingEvents(LocalDateTime now);

    // Optional: ambil upcoming events hanya milik organizer tertentu
    @Query("SELECT e FROM Event e WHERE e.startDate > :now AND e.status = com.eventkita.enums.EventStatus.PUBLISHED AND e.createdBy.id = :organizerId")
    List<Event> findUpcomingEventsByOrganizer(LocalDateTime now, Long organizerId);


    @Query("SELECT e FROM Event e " +
           "WHERE e.status = :status " +
           "AND (:category IS NULL OR e.category = :category) " +
           "AND (:date IS NULL OR FUNCTION('DATE', e.startDate) = FUNCTION('DATE', :date)) " +
           "AND (:location IS NULL OR LOWER(e.location) LIKE LOWER(CONCAT('%', :location, '%')))" +
           "ORDER BY e.startDate ASC")
    List<Event> findByFilters(
            @Param("status") EventStatus status,
            @Param("category") EventCategory category,
            @Param("date") String date,
            @Param("location") String location
    );


}
