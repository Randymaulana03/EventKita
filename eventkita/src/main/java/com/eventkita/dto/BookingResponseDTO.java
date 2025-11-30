package com.eventkita.dto;

import com.eventkita.entity.Booking;
import com.eventkita.enums.BookingStatus;
import com.eventkita.enums.TicketType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class BookingResponseDTO {
    private Long id;
    private Long userId;
    private String userName;
    private Long eventId;
    private String eventTitle;
    private TicketType ticketType;
    private Integer quantity;
    private Double totalAmount;
    private BookingStatus status;
    private String kodeBooking;  // Payment code
    private String qrCode;
    private LocalDateTime createdAt;
    private LocalDateTime paidAt;
    private LocalDateTime expireTime;

    public BookingResponseDTO(Booking booking) {
        this.id = booking.getId();
        this.userId = booking.getUser().getId();
        this.userName = booking.getUser().getFullName();
        this.eventId = booking.getEvent().getId();
        this.eventTitle = booking.getEvent().getTitle();
        this.ticketType = booking.getTicket().getType();
        this.quantity = booking.getQuantity();
        this.totalAmount = booking.getTotalAmount();
        this.status = booking.getStatus();
        this.kodeBooking = booking.getKodeBooking();
        this.qrCode = booking.getQrCode();
        this.createdAt = booking.getCreatedAt();
        this.paidAt = booking.getPaidAt();
        this.expireTime = booking.getExpireTime();
    }
}
