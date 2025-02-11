package com.trial.loan_api.dto.response;

import com.trial.loan_api.model.Loan;

import java.time.LocalDate;

public class LoanInstallmentResponse {
    private Double amount;
    private Double paidAmount;
    private LocalDate dueDate;
    private LocalDate paymentDate;
    private Boolean isPaid;

    public Double getAmount() {
        return amount;
    }

    public Double getPaidAmount() {
        return paidAmount;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public Boolean getPaid() {
        return isPaid;
    }

    public LoanInstallmentResponse(Double amount, Double paidAmount, LocalDate dueDate, LocalDate paymentDate, Boolean isPaid) {
        this.amount = amount;
        this.paidAmount = paidAmount;
        this.dueDate = dueDate;
        this.paymentDate = paymentDate;
        this.isPaid = isPaid;
    }
}
