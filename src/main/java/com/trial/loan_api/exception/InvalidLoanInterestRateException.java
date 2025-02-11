package com.trial.loan_api.exception;

public class InvalidLoanInterestRateException extends RuntimeException {
    public InvalidLoanInterestRateException(String message) {
        super(message);
    }
}
