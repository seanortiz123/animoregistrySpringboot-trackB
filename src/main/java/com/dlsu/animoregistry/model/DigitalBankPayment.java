package com.dlsu.animoregistry.model;

import java.util.UUID;

public class DigitalBankPayment implements PaymentMethod {

    @Override
    public String processPayment(double amount, String payerName) {
        String referenceNo = "DB-" + UUID.randomUUID().toString().substring(0, 10).toUpperCase();
        return String.format(
                "Digital bank transfer of PHP %.2f from %s is being processed. " +
                "Reference No: %s. Funds typically settle within 1-2 banking days.",
                amount, payerName, referenceNo);
    }
}
