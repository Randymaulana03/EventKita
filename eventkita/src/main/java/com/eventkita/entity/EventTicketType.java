package com.eventkita.entity;

import com.eventkita.enums.TicketType;
import jakarta.persistence.*;
import lombok.*;

/**
 * Entity yang menyimpan konfigurasi tipe tiket yang tersedia untuk sebuah event.
 * Organizer dapat memilih tipe tiket apa saja yang akan disediakan (REGULAR, VIP, VVIP)
 * beserta harga dan kuota masing-masing.
 */
@Entity
@Table(name = "event_ticket_types",
        uniqueConstraints = @UniqueConstraint(columnNames = {"event_id", "ticket_type"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventTicketType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @Enumerated(EnumType.STRING)
    @Column(name = "ticket_type", nullable = false)
    private TicketType ticketType;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private Integer quota;

    @Column(name = "available_quota", nullable = false)
    private Integer availableQuota;

    @PrePersist
    protected void onCreate() {
        if (this.availableQuota == null) {
            this.availableQuota = this.quota;
        }
    }

    /**
     * Mengurangi kuota yang tersedia saat ada booking
     */
    public boolean reduceQuota(int amount) {
        if (this.availableQuota >= amount) {
            this.availableQuota -= amount;
            return true;
        }
        return false;
    }

    /**
     * Menambah kembali kuota saat booking dibatalkan
     */
    public void restoreQuota(int amount) {
        this.availableQuota += amount;
        if (this.availableQuota > this.quota) {
            this.availableQuota = this.quota;
        }
    }
}
