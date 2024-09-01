package cred.com.paymentrecommendation.utils;


import cred.com.paymentrecommendation.enums.LineOfBusiness;
import cred.com.paymentrecommendation.enums.PaymentInstrumentType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PaymentConstants {

    public static Map<LineOfBusiness, List<PaymentInstrumentType>> BUSINESS_TO_PAYMENT_TYPE_MAP = new HashMap<>();

    static {
        BUSINESS_TO_PAYMENT_TYPE_MAP.put(LineOfBusiness.CREDIT_CARD_BILL_PAYMENT,
                List.of(PaymentInstrumentType.UPI, PaymentInstrumentType.DEBIT_CARD, PaymentInstrumentType.NETBANKING));
        BUSINESS_TO_PAYMENT_TYPE_MAP.put(LineOfBusiness.COMMERCE,
                List.of(PaymentInstrumentType.UPI, PaymentInstrumentType.DEBIT_CARD, PaymentInstrumentType.CREDIT_CARD));
        BUSINESS_TO_PAYMENT_TYPE_MAP.put(LineOfBusiness.INVESTMENT,
                List.of(PaymentInstrumentType.UPI, PaymentInstrumentType.DEBIT_CARD, PaymentInstrumentType.NETBANKING));

        BUSINESS_TO_PAYMENT_TYPE_MAP.put(LineOfBusiness.TRAVEL,
                List.of(PaymentInstrumentType.UPI, PaymentInstrumentType.DEBIT_CARD, PaymentInstrumentType.NETBANKING));
    }

    public static Map<LineOfBusiness, Double> BUSINESS_LIMIT_MAP = new HashMap<>();

    static {
        BUSINESS_LIMIT_MAP.put(LineOfBusiness.CREDIT_CARD_BILL_PAYMENT, 200000.0);
        BUSINESS_LIMIT_MAP.put(LineOfBusiness.COMMERCE, 200000.0);
        BUSINESS_LIMIT_MAP.put(LineOfBusiness.INVESTMENT, 200000.0);
        BUSINESS_LIMIT_MAP.put(LineOfBusiness.TRAVEL, 200000.0);
    }
}
