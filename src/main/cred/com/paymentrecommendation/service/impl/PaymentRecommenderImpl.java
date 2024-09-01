package cred.com.paymentrecommendation.service.impl;

import cred.com.paymentrecommendation.models.PaymentInstrument;
import cred.com.paymentrecommendation.comparator.PaymentInstrumentComparator;
import cred.com.paymentrecommendation.enums.PaymentInstrumentType;
import cred.com.paymentrecommendation.models.Cart;
import cred.com.paymentrecommendation.models.User;
import cred.com.paymentrecommendation.service.PaymentRecommender;
import cred.com.paymentrecommendation.utils.PaymentConstants;
import cred.com.paymentrecommendation.utils.PaymentPriority;
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
        Map<PaymentInstrumentType, Integer> paymentInstrumentTypePriority = PaymentPriority.PAYMENT_INSTRUMENT_TYPE_PRIORITY.get(cart.getLineOfBusiness());
        Double LIMIT = PaymentConstants.BUSINESS_LIMIT_MAP.get(cart.getLineOfBusiness());
        if (Objects.nonNull(cart.getCartDetail()) && cart.getCartDetail().getCartAmount() > LIMIT) {
            return List.of();
        }
        List<PaymentInstrumentType> eligiblePaymentTypeForBusiness = PaymentConstants.BUSINESS_TO_PAYMENT_TYPE_MAP.getOrDefault(cart.getLineOfBusiness(), List.of());
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
}
