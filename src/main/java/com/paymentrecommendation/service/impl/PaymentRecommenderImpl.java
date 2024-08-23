package com.paymentrecommendation.service.impl;

import com.paymentrecommendation.enums.PaymentInstrumentType;
import com.paymentrecommendation.exception.BadRequest;
import com.paymentrecommendation.models.Cart;
import com.paymentrecommendation.models.PaymentInstrument;
import com.paymentrecommendation.models.User;
import com.paymentrecommendation.service.PaymentRecommender;
import com.paymentrecommendation.utils.ErrorMessages;
import com.paymentrecommendation.utils.PaymentConstants;
import com.paymentrecommendation.comparator.PaymentInstrumentComparator;
import com.paymentrecommendation.utils.PaymentPriority;
import com.paymentrecommendation.validators.PaymentValidator;

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
