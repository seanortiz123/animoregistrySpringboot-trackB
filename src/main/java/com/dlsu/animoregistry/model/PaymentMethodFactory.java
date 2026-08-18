package com.dlsu.animoregistry.model;

/**
 * Simple factory that hands back the correct PaymentMethod implementation
 * for a given PaymentType, so the rest of the app only ever depends on the
 * PaymentMethod abstraction and never on a concrete class directly.
 */
public class PaymentMethodFactory {

    private PaymentMethodFactory() {
    }

    public static PaymentMethod from(PaymentType type) {
        return switch (type) {
            case CASH -> new CashPayment();
            case DIGITAL_BANK -> new DigitalBankPayment();
        };
    }
}
