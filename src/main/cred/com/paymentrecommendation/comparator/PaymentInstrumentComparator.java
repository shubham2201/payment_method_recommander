package cred.com.paymentrecommendation.comparator;


import cred.com.paymentrecommendation.enums.PaymentInstrumentType;
import cred.com.paymentrecommendation.models.PaymentInstrument;

import java.util.Comparator;
import java.util.Map;

public class PaymentInstrumentComparator implements Comparator<PaymentInstrument> {

    private final Map<PaymentInstrumentType, Integer> paymentInstrumentTypePriority;

    public PaymentInstrumentComparator(Map<PaymentInstrumentType, Integer> paymentInstrumentTypePriority) {
        this.paymentInstrumentTypePriority = paymentInstrumentTypePriority;
    }

    @Override
    public int compare(PaymentInstrument paymentInstrument1, PaymentInstrument paymentInstrument2) {
        int priorityComparison = Integer.compare(
                paymentInstrumentTypePriority.getOrDefault(paymentInstrument1.getPaymentInstrumentType(), Integer.MAX_VALUE),
                paymentInstrumentTypePriority.getOrDefault(paymentInstrument2.getPaymentInstrumentType(), Integer.MAX_VALUE)
        );

        if (priorityComparison != 0) {
            return priorityComparison;
        }

        return Double.compare(paymentInstrument2.getRelevanceScore(), paymentInstrument1.getRelevanceScore());
    }
}

