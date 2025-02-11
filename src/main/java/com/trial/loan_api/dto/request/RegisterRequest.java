package com.trial.loan_api.dto.request;

public class RegisterRequest {
    private String username;
    private String password;
    private String name;
    private String surname;
    private Double creditLimit;
    private Double usedCreditLimit;

    public Double getUsedCreditLimit() {
        return usedCreditLimit;
    }

    public void setUsedCreditLimit(Double usedCreditLimit) {
        this.usedCreditLimit = usedCreditLimit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Double getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(Double creditLimit) {
        this.creditLimit = creditLimit;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
