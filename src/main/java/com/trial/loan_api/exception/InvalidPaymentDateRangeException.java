package com.trial.loan_api.exception;

public class InvalidPaymentDateRangeException extends RuntimeException{
    public InvalidPaymentDateRangeException(String message){super (message);}
}
