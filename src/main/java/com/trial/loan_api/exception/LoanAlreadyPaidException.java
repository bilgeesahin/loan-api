package com.trial.loan_api.exception;

public class LoanAlreadyPaidException extends RuntimeException {
    public LoanAlreadyPaidException(String message) {
        super(message);
    }
}
