package com.trial.loan_api.exception;

public class InvalidLoanInstallmentException extends RuntimeException {
    public InvalidLoanInstallmentException(String message) {
        super(message);
    }
}
