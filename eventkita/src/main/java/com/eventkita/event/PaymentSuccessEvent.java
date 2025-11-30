package com.eventkita.event;

import com.eventkita.entity.Payment;
import org.springframework.context.ApplicationEvent;

public class PaymentSuccessEvent extends ApplicationEvent {
    private final Payment payment;

    public PaymentSuccessEvent(Object source, Payment payment) {
        super(source);
        this.payment = payment;
    }

    public Payment getPayment() {
        return payment;
    }
}
