package com.trial.loan_api.repository;

import com.trial.loan_api.model.LoanInstallment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanInstallmentRepository extends JpaRepository<LoanInstallment, Long> {
    List<LoanInstallment> findByLoanIdAndIsPaid(Long loanId, boolean isPaid);
    List<LoanInstallment> findByLoanIdOrderByDueDate(Long loanId);
}
