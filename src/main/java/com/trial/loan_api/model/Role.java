package com.trial.loan_api.model;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    public RoleType name;

    @ManyToMany(mappedBy = "roles")
    private Set<BankUser> users = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RoleType getName() {
        return name;
    }

    public void setName(RoleType name) {
        this.name = name;
    }

    public Set<BankUser> getUsers() {
        return users;
    }

    public void setUsers(Set<BankUser> users) {
        this.users = users;
    }

    public enum RoleType {
        ADMIN, CUSTOMER
    }
}