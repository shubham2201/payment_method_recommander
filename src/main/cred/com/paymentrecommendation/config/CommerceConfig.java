package cred.com.paymentrecommendation.config;

import cred.com.paymentrecommendation.enums.PaymentInstrumentType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommerceConfig implements LobConfig{
    @Override
    public List<PaymentInstrumentType> getEnabledPaymentMethods() {
        return List.of(PaymentInstrumentType.UPI, PaymentInstrumentType.DEBIT_CARD, PaymentInstrumentType.CREDIT_CARD);
    }

    @Override
    public Map<PaymentInstrumentType, Double> paymentMethodLimits() {
        Map<PaymentInstrumentType, Double> BUSINESS_LIMIT_MAP = new HashMap<>();
        BUSINESS_LIMIT_MAP.put(PaymentInstrumentType.UPI, 200000.0);
        BUSINESS_LIMIT_MAP.put(PaymentInstrumentType.CREDIT_CARD, 200000.0);
        BUSINESS_LIMIT_MAP.put(PaymentInstrumentType.DEBIT_CARD, 200000.0);
        return BUSINESS_LIMIT_MAP;
    }

    @Override
    public Map<PaymentInstrumentType, Integer> paymentMethodRelevance() {
        return Map.of(
                PaymentInstrumentType.CREDIT_CARD, 1,
                PaymentInstrumentType.UPI, 2,
                PaymentInstrumentType.DEBIT_CARD, 3
        );
    }
}
