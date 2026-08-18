package com.dlsu.animoregistry.model;

/**
 * OOP CONCEPT - ABSTRACTION:
 * Callers only need to know that a PaymentMethod can processPayment(). They
 * don't need to know HOW a cash payment is recorded versus how a digital
 * bank transfer is processed, since that complexity is hidden behind this
 * interface and handled inside each implementing class.
 */
public interface PaymentMethod {
    String processPayment(double amount, String payerName);
}
