package com.eventkita.strategy.payment;

import com.eventkita.entity.Ticket;

public class BankTransferPaymentStrategy implements PaymentStrategy {
    @Override
    public boolean pay(Ticket ticket, double amount) {
        // implementasi payment via transfer
        return true;
    }
}
