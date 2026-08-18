package com.dlsu.animoregistry.dto;

public class PaymentRequest {

    private String payerName;
    private double amountOverride; // optional, 0 means use org's default fee

    public PaymentRequest() {
    }

    public String getPayerName() {
        return payerName;
    }

    public void setPayerName(String payerName) {
        this.payerName = payerName;
    }

    public double getAmountOverride() {
        return amountOverride;
    }

    public void setAmountOverride(double amountOverride) {
        this.amountOverride = amountOverride;
    }
}
