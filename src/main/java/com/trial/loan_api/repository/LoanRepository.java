package com.trial.loan_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.trial.loan_api.model.Loan;

import java.util.List;
import java.util.Optional;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {
    List<Loan> findByCustomerId(Long customerId);
    Optional<Loan> findById(Long loanId);
    List<Loan> findByCustomerIdAndIsPaid(Long customerId, boolean isPaid);
}
