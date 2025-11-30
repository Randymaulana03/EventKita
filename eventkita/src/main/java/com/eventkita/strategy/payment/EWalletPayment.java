package com.eventkita.strategy.payment;

import com.eventkita.entity.Ticket;

public class EWalletPayment implements PaymentStrategy {

    private String eWalletProvider; // misal: "OVO", "Dana", "GoPay"

    public EWalletPayment(String eWalletProvider) {
        this.eWalletProvider = eWalletProvider;
    }

    @Override
    public boolean pay(Ticket ticket, double amount) {
        // Simulasi proses pembayaran e-wallet
        System.out.println("Processing " + amount + " via " + eWalletProvider + " for ticket " + ticket.getId());

        // Di sini bisa ditambahkan integrasi nyata ke provider
        // Misal: cek saldo, panggil API, dsb.

        // Return true jika pembayaran sukses
        return true;
    }
}
