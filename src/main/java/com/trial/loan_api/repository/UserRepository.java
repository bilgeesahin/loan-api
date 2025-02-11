package com.trial.loan_api.repository;

import com.trial.loan_api.model.BankUser;
import com.trial.loan_api.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<BankUser, Long> {
    Optional<BankUser> findByUsername(String username);
}
