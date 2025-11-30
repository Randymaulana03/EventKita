package com.eventkita.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "komisi")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Komisi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Persentase komisi, misal 10 = 10%
    @Column(nullable = false)
    private Double persentase;

    // Total komisi yang diambil dari booking
    @Column(nullable = false)
    private Double amount;

    // Komisi ini berasal dari booking mana
    @OneToOne
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;

    // Event mana yang menerima pendapatan (digunakan untuk laporan)
    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    // ===== BUSINESS METHODS =====
    
    // Hitung komisi secara otomatis (nanti dipanggil service)
    public static Double calculateCommission(Double totalPrice, Double persentase) {
        if (totalPrice == null || persentase == null) return 0.0;
        return (totalPrice * persentase) / 100.0;
    }
    
    // Helper method untuk get organizer revenue
    public Double getOrganizerRevenue() {
        return booking != null ? booking.getTotalAmount() - amount : 0.0;
    }
    
    // Auto-calculate saat set persentase
    public void setPersentase(Double persentase) {
        this.persentase = persentase;
        if (booking != null && booking.getTotalAmount() != null) {
            this.amount = calculateCommission(booking.getTotalAmount(), persentase);
        }
    }
}