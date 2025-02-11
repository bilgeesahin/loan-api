package com.trial.loan_api.exception;

public class InsufficientCreditLimitException extends RuntimeException {
    public InsufficientCreditLimitException(String message) {
        super(message);
    }
}
