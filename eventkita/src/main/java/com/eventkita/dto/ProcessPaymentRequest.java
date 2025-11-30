package com.eventkita.dto;

public class ProcessPaymentRequest {
    private String paymentMethod;
    private String paymentCode;

    public ProcessPaymentRequest() {}

    public ProcessPaymentRequest(String paymentMethod, String paymentCode) {
        this.paymentMethod = paymentMethod;
        this.paymentCode = paymentCode;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentCode() {
        return paymentCode;
    }

    public void setPaymentCode(String paymentCode) {
        this.paymentCode = paymentCode;
    }
}
