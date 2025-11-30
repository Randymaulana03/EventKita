package com.eventkita.strategy.payment;

import com.eventkita.entity.Ticket;

public interface PaymentStrategy {
    boolean pay(Ticket ticket, double amount);
}
