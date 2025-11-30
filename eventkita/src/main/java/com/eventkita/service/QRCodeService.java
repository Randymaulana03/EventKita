package com.eventkita.service;

/**
 * Service untuk generate dan validate QR code tiket
 */
public interface QRCodeService {
    
    /**
     * Generate QR code untuk booking/e-ticket
     */
    String generateQRCode(Long bookingId);
    
    /**
     * Validate QR code saat entry acara
     */
    boolean validateQRCode(String qrCode);
    
    /**
     * Mark ticket as used setelah validation
     */
    void markTicketAsUsed(String qrCode);
}
