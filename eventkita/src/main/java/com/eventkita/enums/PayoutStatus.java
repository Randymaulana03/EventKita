package com.eventkita.enums;

public enum PayoutStatus {
    PENDING,      // Menunggu verifikasi admin
    PROCESSING,   // Sedang diproses transfer
    COMPLETED,    // Sudah dicairkan
    FAILED,       // Gagal (saldo tidak cukup, data bank salah, dll)
    CANCELLED     // Dibatalkan
}