package com.eventkita.entity;

import com.eventkita.enums.TicketType;
import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity
@Table(name = "tickets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Jenis tiket (REGULAR / VIP / VVIP atau lainnya)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TicketType type;

    // Harga tiket
    @Column(nullable = false)
    private Double price;

    // Kuota tiket
    @Column(nullable = false)
    private Integer quota;

    // Event pemilik tiket
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @Column(unique = true)
    private String qrCode;

    public void generateQRCode() {
        this.qrCode = UUID.randomUUID().toString();
    }
}
