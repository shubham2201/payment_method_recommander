package cred.com.paymentrecommendation.service.impl;

import cred.com.paymentrecommendation.config.CommerceConfig;
import cred.com.paymentrecommendation.config.CreditCardBillPaymentConfig;
import cred.com.paymentrecommendation.config.InvestmentConfig;
import cred.com.paymentrecommendation.config.LobConfig;
import cred.com.paymentrecommendation.enums.LineOfBusiness;
import cred.com.paymentrecommendation.models.PaymentInstrument;
import cred.com.paymentrecommendation.comparator.PaymentInstrumentComparator;
import cred.com.paymentrecommendation.enums.PaymentInstrumentType;
import cred.com.paymentrecommendation.models.Cart;
import cred.com.paymentrecommendation.models.User;
import cred.com.paymentrecommendation.service.PaymentRecommender;
import cred.com.paymentrecommendation.validators.PaymentValidator;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class PaymentRecommenderImpl implements PaymentRecommender {

    @Override
    public List<PaymentInstrument> recommendPaymentInstruments(User user, Cart cart) {
        PaymentValidator.validateLineOfBusiness(cart);
        LobConfig lobConfig = getLobConfig(cart.getLineOfBusiness());
        Map<PaymentInstrumentType, Integer> paymentInstrumentTypePriority = lobConfig.paymentMethodRelevance();
        List<PaymentInstrumentType> eligiblePaymentTypeForBusiness = lobConfig.getEnabledPaymentMethods();
        Map<PaymentInstrumentType, Double> limit = lobConfig.paymentMethodLimits();
        eligiblePaymentTypeForBusiness = eligiblePaymentTypeForBusiness.stream().filter(pm ->
                Objects.nonNull(cart.getCartDetail()) && Objects.nonNull(cart.getCartDetail().getCartAmount()) && limit.get(pm) >= cart.getCartDetail().getCartAmount()
        ).collect(Collectors.toList());
        List<PaymentInstrument> fetchEligiblePaymentTypesForUser = fetchEligiblePaymentTypesForUser(user, eligiblePaymentTypeForBusiness);
        Comparator<PaymentInstrument> comparator = new PaymentInstrumentComparator(paymentInstrumentTypePriority);
        fetchEligiblePaymentTypesForUser.sort(comparator);
        return fetchEligiblePaymentTypesForUser;
    }

    private List<PaymentInstrument> fetchEligiblePaymentTypesForUser(User user, List<PaymentInstrumentType> eligiblePaymentTypeForBusiness) {
        List<PaymentInstrumentType> eligiblePaymentTypeForUser = eligiblePaymentTypeForBusiness.stream()
                .filter(paymentInstrumentType ->
                        !PaymentInstrumentType.UPI.equals(paymentInstrumentType) ||
                                (Objects.nonNull(user.getUserContext()) && Objects.nonNull(user.getUserContext().getDeviceContext())
                                        &&  user.getUserContext().getDeviceContext().isUpiEnabled())
                ).collect(Collectors.toList());
        return user.getUserPaymentInstrument().getPaymentInstruments().stream()
                .filter(paymentInstrument -> eligiblePaymentTypeForUser.contains(paymentInstrument.getPaymentInstrumentType()))
                .collect(Collectors.toList());
    }

    private LobConfig getLobConfig(LineOfBusiness lob) {
        switch(lob) {
            case CREDIT_CARD_BILL_PAYMENT: return new CreditCardBillPaymentConfig();
            case COMMERCE: return new CommerceConfig();
            case INVESTMENT: return new InvestmentConfig();
            default: throw new RuntimeException("Invalid LOB");
        }
    }
}
