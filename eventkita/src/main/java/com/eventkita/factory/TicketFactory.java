package com.eventkita.factory;

import com.eventkita.entity.Event;
import com.eventkita.entity.EventTicketType;
import com.eventkita.entity.Ticket;
import com.eventkita.enums.TicketType;

import java.util.UUID;

/**
 * Factory Pattern untuk membuat Ticket.
 * Sekarang mendukung pembuatan ticket berdasarkan EventTicketType yang dipilih organizer.
 */
public class TicketFactory {

    /**
     * Membuat ticket dari EventTicketType yang sudah dikonfigurasi organizer
     */
    public static Ticket createTicketFromEventTicketType(EventTicketType eventTicketType) {
        Ticket ticket = new Ticket();
        ticket.setEvent(eventTicketType.getEvent());
        ticket.setType(eventTicketType.getTicketType());
        ticket.setPrice(eventTicketType.getPrice());
        ticket.setQuota(eventTicketType.getQuota());
        ticket.setQrCode(UUID.randomUUID().toString());
        
        return ticket;
    }

    /**
     * Membuat ticket secara manual dengan parameter lengkap (untuk backward compatibility)
     */
    public static Ticket createTicket(Event event, TicketType type, double price, int quota) {
        Ticket ticket = new Ticket();
        ticket.setEvent(event);
        ticket.setPrice(price);
        ticket.setQuota(quota);
        ticket.setType(type);
        ticket.setQrCode(UUID.randomUUID().toString());

        return ticket;
    }

    /**
     * Convenience overload: create by string type name (e.g. "REGULAR", "VIP", "VVIP")
     */
    public static Ticket createTicket(Event event, String typeName, double price, int quota) {
        TicketType type;
        try {
            type = TicketType.valueOf(typeName.toUpperCase());
        } catch (IllegalArgumentException ex) {
            // fallback to REGULAR
            type = TicketType.REGULAR;
        }

        return createTicket(event, type, price, quota);
    }
}
