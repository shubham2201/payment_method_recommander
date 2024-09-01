package cred.com.paymentrecommendation.service;

import cred.com.paymentrecommendation.enums.Issuer;
import cred.com.paymentrecommendation.enums.LineOfBusiness;
import cred.com.paymentrecommendation.enums.PaymentInstrumentType;
import cred.com.paymentrecommendation.models.*;
import cred.com.paymentrecommendation.service.impl.PaymentRecommenderImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

class PaymentRecommenderImplTest {
    private PaymentRecommender paymentRecommender;

    @BeforeEach
    void setup() {
        //TODO: Setup paymentRecommender
        paymentRecommender = new PaymentRecommenderImpl();
    }

    @Test
    void testCommerceWithNoInstruments() {
        final User user = new User(UUID.randomUUID().toString(), null, new UserPaymentInstrument(new ArrayList<>()));
        final Cart cart = new Cart(LineOfBusiness.COMMERCE, null);
        final List<PaymentInstrument> actualList = paymentRecommender.recommendPaymentInstruments(user, cart);
        assertTrue(actualList.isEmpty());
    }

    @Test
    void testCommerceWithNetBankingInstrument() {
        final PaymentInstrument netBankingInstrument = createNetBankingPaymentInstrument();
        final User user = new User(UUID.randomUUID().toString(), null, new UserPaymentInstrument(Collections.singletonList(netBankingInstrument)));
        final Cart cart = new Cart(LineOfBusiness.COMMERCE, new CartDetail(1000d, Collections.singletonList(getCartItem(1000d))));
        final List<PaymentInstrument> actualList = paymentRecommender.recommendPaymentInstruments(user, cart);
        assertTrue(actualList.isEmpty());
    }

    @Test
    void testCommerceWithUPIInstrumentButNotUPIEnabled() {
        final PaymentInstrument upiPaymentInstrument = createUPIPaymentInstrument();
        final User user = new User(UUID.randomUUID().toString(), new UserContext(new DeviceContext(false)),
                new UserPaymentInstrument(Collections.singletonList(upiPaymentInstrument)));
        final Cart cart = new Cart(LineOfBusiness.COMMERCE, new CartDetail(1000d, Collections.singletonList(getCartItem(1000d))));
        final List<PaymentInstrument> actualList = paymentRecommender.recommendPaymentInstruments(user, cart);
        assertTrue(actualList.isEmpty());
    }

    @Test
    void testCommerceWithUPIInstrumentButLimitBreached() {
        final PaymentInstrument upiPaymentInstrument = createUPIPaymentInstrument();
        final User user = new User(UUID.randomUUID().toString(), new UserContext(new DeviceContext(true)),
                new UserPaymentInstrument(Collections.singletonList(upiPaymentInstrument)));
        final Cart cart = new Cart(LineOfBusiness.COMMERCE, new CartDetail(1000001d, Collections.singletonList(getCartItem(1000001d))));
        final List<PaymentInstrument> actualList = paymentRecommender.recommendPaymentInstruments(user, cart);
        assertTrue(actualList.isEmpty());
    }

    @Test
    void testCommerceWithDebitCardButLimitBreached() {
        final PaymentInstrument debitCardInstrument = createDebitCardPaymentInstrument();
        final User user = new User(UUID.randomUUID().toString(), new UserContext(new DeviceContext(false)),
                new UserPaymentInstrument(Collections.singletonList(debitCardInstrument)));
        final Cart cart = new Cart(LineOfBusiness.COMMERCE, new CartDetail(2000001d, Collections.singletonList(getCartItem(2000001d))));
        final List<PaymentInstrument> actualList = paymentRecommender.recommendPaymentInstruments(user, cart);
        assertTrue(actualList.isEmpty());
    }

    @Test
    void testCommerceWithCreditCardButLimitBreached() {
        final PaymentInstrument creditCardPaymentInstrument = createCreditCardPaymentInstrument();
        final User user = new User(UUID.randomUUID().toString(), new UserContext(new DeviceContext(false)),
                new UserPaymentInstrument(Collections.singletonList(creditCardPaymentInstrument)));
        final Cart cart = new Cart(LineOfBusiness.COMMERCE, new CartDetail(2000001d, Collections.singletonList(getCartItem(2000001d))));
        final List<PaymentInstrument> actualList = paymentRecommender.recommendPaymentInstruments(user, cart);
        assertTrue(actualList.isEmpty());
    }

    @Test
    void testCommerceHappyScenario() {
        final PaymentInstrument cardInstrument1 = createCreditCardPaymentInstrument();
        final PaymentInstrument upiPaymentInstrument1 = createUPIPaymentInstrument();
        final PaymentInstrument upiPaymentInstrument2 = createUPIPaymentInstrument();
        upiPaymentInstrument2.setIssuer(Issuer.SBI);
        upiPaymentInstrument2.setRelevanceScore(0.88);
        final PaymentInstrument netBankingPaymentInstrument = createNetBankingPaymentInstrument();
        final PaymentInstrument debitCardInstrument = createDebitCardPaymentInstrument();

        final User user = new User(UUID.randomUUID().toString(), new UserContext(new DeviceContext(true)), new UserPaymentInstrument(
                Arrays.asList(cardInstrument1, upiPaymentInstrument1, upiPaymentInstrument2, netBankingPaymentInstrument, debitCardInstrument)));
        final List<PaymentInstrument> expectedInstrumentList = Arrays.asList(cardInstrument1, upiPaymentInstrument2, upiPaymentInstrument1,
                debitCardInstrument);
        final Cart cart = new Cart(LineOfBusiness.COMMERCE, new CartDetail(1000d, Collections.singletonList(getCartItem(1000d))));
        final List<PaymentInstrument> actualList = paymentRecommender.recommendPaymentInstruments(user, cart);
        assertEquals(4, actualList.size());
        assertEquals(expectedInstrumentList, actualList);
    }

    private PaymentInstrument createCreditCardPaymentInstrument() {
        return new PaymentInstrument(PaymentInstrumentType.CREDIT_CARD, "credit_card_number", Issuer.HDFC, "HDFC Diner", 0.7);
    }

    private PaymentInstrument createUPIPaymentInstrument() {
        return new PaymentInstrument(PaymentInstrumentType.UPI, "upi_handle", Issuer.SBI, "SBI UPI", 0.85);
    }

    private PaymentInstrument createNetBankingPaymentInstrument() {
        return new PaymentInstrument(PaymentInstrumentType.NETBANKING, "bank_account_number", Issuer.SBI, "Savings Account", 0.86);
    }

    private PaymentInstrument createDebitCardPaymentInstrument() {
        return new PaymentInstrument(PaymentInstrumentType.DEBIT_CARD, "debit_card_number", Issuer.SBI, "SBI Debit Card", 0.91);
    }

    private CartItem getCartItem(final Double amount) {
        return new CartItem(UUID.randomUUID().toString(), "Samsung Television", amount);
    }
}
