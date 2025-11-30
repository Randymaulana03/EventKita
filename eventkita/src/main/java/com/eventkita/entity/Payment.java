package com.eventkita.entity;

import com.eventkita.enums.PaymentMethod;
import com.eventkita.enums.PaymentStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relasi ke Booking
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private PaymentStatus status = PaymentStatus.PENDING;

    @Column(nullable = false)
    private Double amount;

    // Nomor referensi dari payment gateway (Midtrans, Xendit, dll)
    @Column(unique = true)
    private String referenceId;

    // Kode pembayaran untuk metode QRIS (20 karakter random)
    @Column(unique = true)
    private String paymentCode;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
    
    private LocalDateTime paidAt;

    // ===== BUSINESS METHODS =====
    public Boolean isSuccess() {
        return this.status == PaymentStatus.SUCCESS;
    }

    public Boolean canBeProcessed() {
        return this.status == PaymentStatus.PENDING;
    }

    public void markAsSuccess() {
        this.status = PaymentStatus.SUCCESS;
        this.paidAt = LocalDateTime.now();
    }

    public void markAsFailed() {
        this.status = PaymentStatus.FAILED;
    }
    
    public Boolean isExpired() {
        return this.status == PaymentStatus.EXPIRED;
    }

    public PaymentMethod getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(PaymentMethod paymentMethod) { this.paymentMethod = paymentMethod; }

}