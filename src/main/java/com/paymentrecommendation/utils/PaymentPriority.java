package com.paymentrecommendation.utils;

import com.paymentrecommendation.enums.LineOfBusiness;
import com.paymentrecommendation.enums.PaymentInstrumentType;

import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class PaymentPriority {

    public static final Map<LineOfBusiness, Map<PaymentInstrumentType, Integer>> PAYMENT_INSTRUMENT_TYPE_PRIORITY;
    private static final Set<PaymentInstrumentType> ALL_PAYMENT_INSTRUMENT_TYPES = EnumSet.allOf(PaymentInstrumentType.class);

    static {
        PAYMENT_INSTRUMENT_TYPE_PRIORITY = Collections.unmodifiableMap(initializePaymentInstrumentTypePriority());
    }

    private static Map<LineOfBusiness, Map<PaymentInstrumentType, Integer>> initializePaymentInstrumentTypePriority() {
        Map<LineOfBusiness, Map<PaymentInstrumentType, Integer>> map = new HashMap<>();
        map.put(LineOfBusiness.CREDIT_CARD_BILL_PAYMENT, createCreditCardBillPaymentMap());
        map.put(LineOfBusiness.COMMERCE, createCommercePaymentMap());
        map.put(LineOfBusiness.INVESTMENT, createInvestmentPaymentMap());
        return map;
    }

    private static Map<PaymentInstrumentType, Integer> createCreditCardBillPaymentMap() {
        return Map.of(
                PaymentInstrumentType.UPI, 1,
                PaymentInstrumentType.NETBANKING, 2,
                PaymentInstrumentType.DEBIT_CARD, 3
        );
    }

    private static Map<PaymentInstrumentType, Integer> createCommercePaymentMap() {
        return Map.of(
                PaymentInstrumentType.CREDIT_CARD, 1,
                PaymentInstrumentType.UPI, 2,
                PaymentInstrumentType.DEBIT_CARD, 3
        );
    }

    private static Map<PaymentInstrumentType, Integer> createInvestmentPaymentMap() {
        return Map.of(
                PaymentInstrumentType.UPI, 1,
                PaymentInstrumentType.NETBANKING, 2,
                PaymentInstrumentType.DEBIT_CARD, 3
        );
    }

    private static Map<PaymentInstrumentType, Integer> createTravelPaymentMap() {
        return Map.of(
                PaymentInstrumentType.UPI, 1,
                PaymentInstrumentType.NETBANKING, 2,
                PaymentInstrumentType.DEBIT_CARD, 3
        );
    }
}

