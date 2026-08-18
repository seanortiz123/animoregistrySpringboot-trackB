package com.dlsu.animoregistry.model;

import java.util.UUID;

public class CashPayment implements PaymentMethod {

    @Override
    public String processPayment(double amount, String payerName) {
        String receiptNo = "CASH-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        return String.format(
                "Cash payment of PHP %.2f received from %s. Receipt No: %s. " +
                "Please have this acknowledged by the org treasurer.",
                amount, payerName, receiptNo);
    }
}
