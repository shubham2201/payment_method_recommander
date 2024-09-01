package cred.com.paymentrecommendation.exception;

public class BadRequest extends RuntimeException {

    private String ErrorMessage;

    public BadRequest(String errorMessage) {
        super(errorMessage);
        this.ErrorMessage = errorMessage;
    }
}
