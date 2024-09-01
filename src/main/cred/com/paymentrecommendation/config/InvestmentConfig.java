package cred.com.paymentrecommendation.config;

import cred.com.paymentrecommendation.enums.PaymentInstrumentType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InvestmentConfig implements LobConfig{
    @Override
    public List<PaymentInstrumentType> getEnabledPaymentMethods() {
        return List.of(PaymentInstrumentType.UPI, PaymentInstrumentType.DEBIT_CARD, PaymentInstrumentType.NETBANKING);
    }

    @Override
    public Map<PaymentInstrumentType, Double> paymentMethodLimits() {
        Map<PaymentInstrumentType, Double> BUSINESS_LIMIT_MAP = new HashMap<>();
        BUSINESS_LIMIT_MAP.put(PaymentInstrumentType.UPI, 200000.0);
        BUSINESS_LIMIT_MAP.put(PaymentInstrumentType.DEBIT_CARD, 200000.0);
        BUSINESS_LIMIT_MAP.put(PaymentInstrumentType.NETBANKING, 200000.0);
        return BUSINESS_LIMIT_MAP;
    }

    @Override
    public Map<PaymentInstrumentType, Integer> paymentMethodRelevance() {
        return Map.of(
                PaymentInstrumentType.UPI, 1,
                PaymentInstrumentType.NETBANKING, 2,
                PaymentInstrumentType.DEBIT_CARD, 3
        );
    }
}
