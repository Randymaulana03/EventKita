package com.eventkita.service.impl;

import com.eventkita.entity.Booking;
import com.eventkita.repository.BookingRepository;
import com.eventkita.service.QRCodeService;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Implementasi QR Code Service
 * Generate unique QR code untuk setiap booking dan validate saat entry
 */
@Service
public class QRCodeServiceImpl implements QRCodeService {

    private final BookingRepository bookingRepository;

    public QRCodeServiceImpl(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    @Override
    public String generateQRCode(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        // Generate unique QR code
        String qrCode = "QR-" + bookingId + "-" + UUID.randomUUID().toString().substring(0, 8);
        booking.setQrCode(qrCode);
        bookingRepository.save(booking);

        return qrCode;
    }

    @Override
    public boolean validateQRCode(String qrCode) {
        if (qrCode == null || qrCode.isEmpty()) {
            return false;
        }

        return bookingRepository.findByQrCode(qrCode).isPresent();
    }

    @Override
    public void markTicketAsUsed(String qrCode) {
        Booking booking = bookingRepository.findByQrCode(qrCode)
                .orElseThrow(() -> new RuntimeException("Booking dengan QR code tidak ditemukan"));

        // Mark as used (bisa add status USED ke BookingStatus enum)
        // booking.setStatus(BookingStatus.USED);
        // bookingRepository.save(booking);
    }
}
