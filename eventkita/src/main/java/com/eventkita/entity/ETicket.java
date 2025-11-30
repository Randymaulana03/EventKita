package com.eventkita.entity;

import com.eventkita.enums.TicketStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "e_tickets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ETicket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Kode unik untuk validasi (bukan QR, tapi random code)
    @Column(unique = true, nullable = false)
    private String ticketCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private TicketStatus status = TicketStatus.ACTIVE;

    @Column(nullable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime usedAt;

    // ===== RELATIONSHIPS =====

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;

    // ===== BUSINESS METHODS =====

    public Boolean isUsed() {
        return this.status == TicketStatus.USED;
    }

    public Boolean isValid() {
        return this.status == TicketStatus.ACTIVE;
    }

    public void markAsUsed() {
        this.status = TicketStatus.USED;
        this.usedAt = LocalDateTime.now();
    }

    public String getEventName() {
        return booking != null && booking.getEvent() != null 
            ? booking.getEvent().getTitle() 
            : "Unknown Event";
    }

    public String getAttendeeName() {
        return booking != null && booking.getUser() != null 
            ? booking.getUser().getFullName() 
            : "Unknown Attendee";
    }
}