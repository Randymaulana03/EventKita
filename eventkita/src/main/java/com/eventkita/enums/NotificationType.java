package com.eventkita.enums;

public enum NotificationType {
        BOOKING_SUCCESS,    // Booking berhasil dibuat
        BOOKING_EXPIRED,    // Booking kadaluarsa
        PAYMENT_SUCCESS,    // Pembayaran berhasil
        PAYMENT_FAILED,     // Pembayaran gagal
        ETICKET_ISSUED,     // E-Ticket diterbitkan
        EVENT_UPDATED,      // Event diupdate
        EVENT_CANCELLED,    // Event dibatalkan
        ORGANIZER_VERIFIED, // Organizer terverifikasi
        PAYOUT_REQUESTED,   // Pencairan dana diajukan
        PAYOUT_COMPLETED,   // Pencairan dana selesai
        SYSTEM_ALERT        // Notifikasi sistem
}