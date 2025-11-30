package com.eventkita.strategy.payment;

import com.eventkita.entity.Ticket;

public class CreditCardPaymentStrategy implements PaymentStrategy {
    @Override
    public boolean pay(Ticket ticket, double amount) {
        // implementasi payment via CC
        return true;
    }
}