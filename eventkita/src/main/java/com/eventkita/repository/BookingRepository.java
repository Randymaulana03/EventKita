package com.eventkita.repository;

import com.eventkita.entity.Booking;
import com.eventkita.enums.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUser_Id(Long userId);
    List<Booking> findByEvent_Id(Long eventId);
    List<Booking> findByStatus(BookingStatus status);
    
    @Query("SELECT b FROM Booking b WHERE b.status = 'PENDING' AND b.createdAt < :expiryTime")
    List<Booking> findExpiredBookings(LocalDateTime expiryTime);
    
    Long countByEvent_IdAndStatus(Long eventId, BookingStatus status);
    
    Optional<Booking> findByQrCode(String qrCode);
}