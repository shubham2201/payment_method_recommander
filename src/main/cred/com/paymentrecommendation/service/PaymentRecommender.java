package cred.com.paymentrecommendation.service;

import cred.com.paymentrecommendation.models.PaymentInstrument;
import cred.com.paymentrecommendation.models.Cart;
import cred.com.paymentrecommendation.models.User;
import java.util.List;

public interface PaymentRecommender {
    List<PaymentInstrument> recommendPaymentInstruments(final User user, final Cart cart);
}
