package com.eventkita.dto;

import com.eventkita.entity.Payment;
import com.eventkita.entity.Booking;
import java.time.LocalDateTime;

public class PaymentDTO {
    private Long id;
    private Long bookingId;
    private Double amount;
    private String paymentMethod;
    private String status;
    private String referenceId;
    private String paymentCode;
    private LocalDateTime createdAt;
    private LocalDateTime paidAt;
    private BookingInfoDTO booking;  // Nested booking info

    // Constructor from Payment entity
    public PaymentDTO(Payment payment) {
        this.id = payment.getId();
        this.amount = payment.getAmount();
        this.paymentMethod = payment.getPaymentMethod().name();
        this.status = payment.getStatus().name();
        this.referenceId = payment.getReferenceId();
        this.paymentCode = payment.getPaymentCode();
        this.createdAt = payment.getCreatedAt();
        this.paidAt = payment.getPaidAt();
        
        if (payment.getBooking() != null) {
            Booking b = payment.getBooking();
            this.bookingId = b.getId();
            this.booking = new BookingInfoDTO(b);
        }
    }

    // ===== Nested Booking Info DTO =====
    public static class BookingInfoDTO {
        private Long id;
        private Long userId;
        private Long eventId;
        private Long ticketId;
        private Integer quantity;
        private Double totalAmount;
        private String status;
        private String qrCode;
        private LocalDateTime createdAt;
        private LocalDateTime expireTime;
        private LocalDateTime paidAt;

        public BookingInfoDTO(Booking booking) {
            this.id = booking.getId();
            this.userId = booking.getUser() != null ? booking.getUser().getId() : null;
            this.eventId = booking.getEvent() != null ? booking.getEvent().getId() : null;
            this.ticketId = booking.getTicket() != null ? booking.getTicket().getId() : null;
            this.quantity = booking.getQuantity();
            this.totalAmount = booking.getTotalAmount();
            this.status = booking.getStatus().name();
            this.qrCode = booking.getQrCode();
            this.createdAt = booking.getCreatedAt();
            this.expireTime = booking.getExpireTime();
            this.paidAt = booking.getPaidAt();
        }

        // Getters
        public Long getId() { return id; }
        public Long getUserId() { return userId; }
        public Long getEventId() { return eventId; }
        public Long getTicketId() { return ticketId; }
        public Integer getQuantity() { return quantity; }
        public Double getTotalAmount() { return totalAmount; }
        public String getStatus() { return status; }
        public String getQrCode() { return qrCode; }
        public LocalDateTime getCreatedAt() { return createdAt; }
        public LocalDateTime getExpireTime() { return expireTime; }
        public LocalDateTime getPaidAt() { return paidAt; }
    }

    // Getters
    public Long getId() { return id; }
    public Long getBookingId() { return bookingId; }
    public Double getAmount() { return amount; }
    public String getPaymentMethod() { return paymentMethod; }
    public String getStatus() { return status; }
    public String getReferenceId() { return referenceId; }
    public String getPaymentCode() { return paymentCode; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getPaidAt() { return paidAt; }
    public BookingInfoDTO getBooking() { return booking; }
}

