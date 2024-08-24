package com.paymentrecommendation.validators;

import com.paymentrecommendation.enums.PaymentInstrumentType;
import com.paymentrecommendation.exception.BadRequest;
import com.paymentrecommendation.models.Cart;
import com.paymentrecommendation.utils.ErrorMessages;

import java.util.Objects;

public class PaymentValidator {

    public static void validateLineOfBusiness(Cart cart) {
        if (Objects.isNull(cart.getLineOfBusiness())) {
            throw new BadRequest(ErrorMessages.INVALID_LINE_OF_BUSINESS);
        }
        // other validations
    }
}
