package com.trial.loan_api.dto.response;

public class LoanResponse {
    private Long id;
    private Double loanAmount;
    private Integer numberOfInstallments;
    private Boolean isPaid;
    private String createDate;
    private Double interestRate;

    public LoanResponse(Long id, Double loanAmount, Integer numberOfInstallments, Boolean isPaid, String createDate, Double interestRate) {
        this.id = id;
        this.loanAmount = loanAmount;
        this.numberOfInstallments = numberOfInstallments;
        this.isPaid = isPaid;
        this.createDate = createDate;
        this.interestRate = interestRate;
    }

    public Long getId() {
        return id;
    }

    public Double getLoanAmount() {
        return loanAmount;
    }

    public Integer getNumberOfInstallments() {
        return numberOfInstallments;
    }

    public Boolean getPaid() {
        return isPaid;
    }

    public String getCreateDate() {
        return createDate;
    }

    public Double getInterestRate() {
        return interestRate;
    }
}
