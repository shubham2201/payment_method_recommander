package cred.com.paymentrecommendation.validators;


import cred.com.paymentrecommendation.exception.BadRequest;
import cred.com.paymentrecommendation.models.Cart;
import cred.com.paymentrecommendation.utils.ErrorMessages;

import java.util.Objects;

public class PaymentValidator {

    public static void validateLineOfBusiness(Cart cart) {
        if (Objects.isNull(cart.getLineOfBusiness())) {
            throw new BadRequest(ErrorMessages.INVALID_LINE_OF_BUSINESS);
        }
        // other validations
    }
}
