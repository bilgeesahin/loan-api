package com.trial.loan_api.model;

import jakarta.persistence.*;


@Entity
public class Customer {
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BankUser getUser() {
        return user;
    }

    public void setUser(BankUser user) {
        this.user = user;
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

    public Double getUsedCreditLimit() {
        return usedCreditLimit;
    }

    public void setUsedCreditLimit(Double usedCreditLimit) {
        this.usedCreditLimit = usedCreditLimit;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private BankUser user;
    private String name;
    private String surname;
    private Double creditLimit;
    private Double usedCreditLimit;
}