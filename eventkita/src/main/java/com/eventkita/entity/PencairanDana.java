package com.eventkita.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import com.eventkita.enums.StatusPencairan;


@Entity
@Table(name = "pencairan_dana")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PencairanDana {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Total dana yang dicairkan ke organizer setelah komisi dipotong
    @Column(nullable = false)
    private Double amount;

    // Status pencairan
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusPencairan status;

    // Event mana yang menerima pencairan dana
    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    // Organizer yang menerima dana
    @ManyToOne
    @JoinColumn(name = "organizer_id", nullable = false)
    private Organizer organizer;

    // Waktu pencairan dilakukan
    private LocalDateTime createdAt;

    // Waktu pencairan diselesaikan
    private LocalDateTime completedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.status == null) {
            this.status = StatusPencairan.PENDING;
        }
    }

    // ===== BUSINESS METHODS =====
    public Boolean isCompleted() {
        return this.status == StatusPencairan.COMPLETED;
    }

    public void markAsCompleted() {
        this.status = StatusPencairan.COMPLETED;
        this.completedAt = LocalDateTime.now();
    }

    
}