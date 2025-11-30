package com.eventkita.util;

import com.eventkita.entity.Booking;
import com.eventkita.entity.Event;
import com.eventkita.entity.Ticket;

import java.util.List;
import java.util.stream.Collectors;

public class PriceCalculator {

    // Hitung total harga semua tiket
    public static double calculateTotalRevenue(List<Ticket> tickets) {
        if (tickets == null || tickets.isEmpty()) return 0.0;

        return tickets.stream()
                .mapToDouble(ticket -> ticket.getPrice() * ticket.getQuota())
                .sum();
    }

    // Hitung komisi platform (misal 10% default)
    public static double calculateCommission(double totalRevenue, double commissionRate) {
        return totalRevenue * commissionRate;
    }

    // Hitung revenue bersih untuk organizer
    public static double calculateNetRevenue(double totalRevenue, double commissionRate) {
        double commission = calculateCommission(totalRevenue, commissionRate);
        return totalRevenue - commission;
    }

    // Hitung revenue bersih dari sebuah event
    public static double calculateEventNetRevenue(Event event, double commissionRate) {
        if (event == null || event.getBookings() == null || event.getBookings().isEmpty()) {
            return 0.0;
        }

        // Ambil semua tiket dari booking
        List<Ticket> allTickets = event.getBookings().stream()
                .map(Booking::getTicket)
                .filter(ticket -> ticket != null)
                .distinct()
                .collect(Collectors.toList());

        double totalRevenue = calculateTotalRevenue(allTickets);
        return calculateNetRevenue(totalRevenue, commissionRate);
    }
}
