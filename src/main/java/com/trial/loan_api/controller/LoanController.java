package com.trial.loan_api.controller;

import com.trial.loan_api.dto.request.LoanRequest;
import com.trial.loan_api.dto.request.PaymentRequest;
import com.trial.loan_api.dto.response.LoanInstallmentResponse;
import com.trial.loan_api.dto.response.LoanResponse;
import com.trial.loan_api.model.Customer;
import com.trial.loan_api.model.Loan;
import com.trial.loan_api.repository.CustomerRepository;
import com.trial.loan_api.repository.LoanRepository;
import com.trial.loan_api.security.CustomUserDetails;
import com.trial.loan_api.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/loans")
public class LoanController {
    @Autowired
    private LoanService loanService;

    @Autowired
    private CustomUserDetails customUserDetails;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private LoanRepository loanRepository;

    @PostMapping("/create")
    public ResponseEntity<?> createLoan(@RequestBody LoanRequest loanRequest) {
        if (customUserDetails.isCustomer()) {
            Customer customer = customerRepository.findByUserId(customUserDetails.getId())
                    .orElseThrow(() -> new RuntimeException("Customer not found."));
            if (!customer.getId().equals(loanRequest.getCustomerId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Operations cannot be performed on behalf of another user.");
            }
        }
        try {
            Loan loan = loanService.createLoan(
                    loanRequest.getCustomerId(),
                    loanRequest.getAmount(),
                    loanRequest.getInterestRate(),
                    loanRequest.getNumberOfInstallments()
            );
            return ResponseEntity.ok("Loan creation successful.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<List<LoanResponse>> listLoans(@PathVariable Long customerId) {
        if (customUserDetails.isCustomer()) {
            Customer customer = customerRepository.findByUserId(customUserDetails.getId())
                    .orElseThrow(() -> new RuntimeException("Customer not found."));
            if (!customer.getId().equals(customerId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        }
        return ResponseEntity.ok(loanService.listLoans(customerId));
    }

    @GetMapping("/{loanId}/installments")
    public ResponseEntity<List<LoanInstallmentResponse>> listLoanInstallments(@PathVariable Long loanId) {
        if (customUserDetails.isCustomer()) {
            Customer customer = customerRepository.findByUserId(customUserDetails.getId())
                    .orElseThrow(() -> new RuntimeException("Customer not found."));
            Loan loan = loanRepository.findById(loanId)
                    .orElseThrow(() -> new RuntimeException("Loan not found."));
            if (!customer.getId().equals(loan.getCustomer().getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        }
        return ResponseEntity.ok(loanService.listLoanInstallments(loanId));
    }

    @PutMapping("/{loanId}/payment")
    public ResponseEntity<String> payLoan(@PathVariable Long loanId, @RequestBody PaymentRequest paymentRequest) {
        if (customUserDetails.isCustomer()) {
            Customer customer = customerRepository.findByUserId(customUserDetails.getId())
                    .orElseThrow(() -> new RuntimeException("Customer not found."));
            Loan loan = loanRepository.findById(loanId)
                    .orElseThrow(() -> new RuntimeException("Loan not found."));
            if (!customer.getId().equals(loan.getCustomer().getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        }
        loanService.payLoan(loanId, paymentRequest.getAmountPaid());
        return ResponseEntity.ok("Payment has been made successfully");
    }
}
