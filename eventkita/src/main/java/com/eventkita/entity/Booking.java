package com.eventkita.entity;

import com.eventkita.enums.BookingStatus;
import jakarta.persistence.*;
import lombok.*;


import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // User yang melakukan booking
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Event yang dipesan
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    // Ticket type (diambil dari Ticket)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id", nullable = false)
    private Ticket ticket;

    // berapa tiket yang dibeli
    @Column(nullable = false)
    private Integer quantity;

    // Total harga (price * quantity)
    @Column(nullable = false)
    private Double totalAmount;

    // Status booking: PENDING, PAID, EXPIRED, CANCELLED
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingStatus status;

    // QR code / kode unik tiket
    @Column(unique = true)
    private String qrCode;

    // Kode booking/reference yang menyimpan kode pembayaran (paymentCode)
    @Column(name = "kode_booking", unique = true)
    private String kodeBooking;

    // waktu pembuatan booking
    private LocalDateTime createdAt;

    // waktu pembayaran sukses
    private LocalDateTime paidAt;

    // waktu booking otomatis expire jika belum dibayar
    private LocalDateTime expireTime;
}
