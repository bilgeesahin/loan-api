package com.trial.loan_api.exception;

public class PartialPaymentException extends RuntimeException {
    public PartialPaymentException(String message) {
        super(message);
    }
}
