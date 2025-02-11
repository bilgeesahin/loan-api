package com.trial.loan_api.dto.request;
public class PaymentRequest {
    private Double amountPaid;

    public Double getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(Double amountPaid) {
        this.amountPaid = amountPaid;
    }

    public PaymentRequest(Double amountPaid) {
        this.amountPaid = amountPaid;
    }
}
