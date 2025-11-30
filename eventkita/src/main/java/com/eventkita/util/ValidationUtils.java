package com.eventkita.util;

import com.eventkita.entity.Event;
import com.eventkita.entity.Ticket;

import java.time.LocalDateTime;

public class ValidationUtils {

    // Validasi tiket
    public static void validateTicket(Ticket ticket) {
        if (ticket.getPrice() <= 0) {
            throw new IllegalArgumentException("Ticket price must be positive");
        }
        if (ticket.getQuota() <= 0) {
            throw new IllegalArgumentException("Ticket quota must be positive");
        }
        if (ticket.getEvent() == null) {
            throw new IllegalArgumentException("Ticket must belong to an event");
        }
    }

    // Validasi event
    public static void validateEvent(Event event) {
        if (event.getTitle() == null || event.getTitle().isBlank()) {
            throw new IllegalArgumentException("Event title cannot be empty");
        }
        if (event.getStartDate() == null || event.getEndDate() == null) {
            throw new IllegalArgumentException("Event must have start and end date");
        }
        if (event.getStartDate().isAfter(event.getEndDate())) {
            throw new IllegalArgumentException("Event start date must be before end date");
        }
        if (event.getStartDate().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Event start date must be in the future");
        }
        if (event.getCreatedBy() == null) {
            throw new IllegalArgumentException("Event must have a creator");
        }
    }

    // Validasi jumlah tiket yang dipesan
    public static void validateTicketQuantity(int quantity, int availableQuota) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Ticket quantity must be positive");
        }
        if (quantity > availableQuota) {
            throw new IllegalArgumentException("Ticket quantity exceeds available quota");
        }
    }
}
