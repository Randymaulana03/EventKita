package com.eventkita.service;

import com.eventkita.dto.BookingRequestDTO;
import com.eventkita.entity.Booking;
import com.eventkita.entity.User;
import com.eventkita.enums.BookingStatus;

import java.util.List;
import java.util.Optional;

public interface BookingService {
    Booking createBooking(Booking booking);
    
    /**
     * Membuat booking berdasarkan pilihan ticket type dari user
     */
    Booking createBookingFromRequest(BookingRequestDTO request, User user);

    Optional<Booking> getBookingById(Long id);

    List<Booking> getUserBookings(Long userId);

    List<Booking> getEventBookings(Long eventId);

    Optional<Booking> findByQrCode(String qrCode);

    Booking updateBookingStatus(Long bookingId, BookingStatus status);

    void cancelBooking(Long bookingId);
}
