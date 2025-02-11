package com.trial.loan_api.dto.request;

public class LoanRequest {
    private Long customerId;
    private Double amount;
    private Double interestRate;
    private Integer numberOfInstallments;

    public Long getCustomerId() {
        return customerId;
    }

    public LoanRequest(Long customerId, Double amount, Double interestRate, Integer numberOfInstallments) {
        this.customerId = customerId;
        this.amount = amount;
        this.interestRate = interestRate;
        this.numberOfInstallments = numberOfInstallments;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(Double interestRate) {
        this.interestRate = interestRate;
    }

    public Integer getNumberOfInstallments() {
        return numberOfInstallments;
    }

    public void setNumberOfInstallments(Integer numberOfInstallments) {
        this.numberOfInstallments = numberOfInstallments;
    }
}

