package cred.com.paymentrecommendation.config;

import cred.com.paymentrecommendation.enums.PaymentInstrumentType;

import java.util.*;

public interface LobConfig {

    List<PaymentInstrumentType> getEnabledPaymentMethods();

    Map<PaymentInstrumentType, Double> paymentMethodLimits();

    Map<PaymentInstrumentType, Integer> paymentMethodRelevance();
}
