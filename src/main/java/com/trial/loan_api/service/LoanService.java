package com.trial.loan_api.service;

import com.trial.loan_api.dto.response.LoanInstallmentResponse;
import com.trial.loan_api.dto.response.LoanResponse;
import com.trial.loan_api.exception.*;
import com.trial.loan_api.model.Customer;
import com.trial.loan_api.model.Loan;
import com.trial.loan_api.model.LoanInstallment;
import com.trial.loan_api.repository.CustomerRepository;
import com.trial.loan_api.repository.LoanInstallmentRepository;
import com.trial.loan_api.repository.LoanRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class LoanService {
    @Autowired
    private LoanRepository loanRepository;
    @Autowired
    private LoanInstallmentRepository loanInstallmentRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Transactional
    public Loan createLoan(Long customerId, Double amount, Double interestRate, Integer numberOfInstallments){
        Customer customer = customerRepository.findById(customerId).
                orElseThrow(() -> new RuntimeException("Customer not found"));

        double newUsedLimit = customer.getUsedCreditLimit() + amount;
        if(newUsedLimit > customer.getCreditLimit()){
            throw new InsufficientCreditLimitException("Customer does not have enough credit limit.");
        }
        if(!List.of(6,9,12,24).contains(numberOfInstallments)){
            throw new InvalidLoanInstallmentException("Invalid number of installments. Must be 6, 9, 12, or 24.");
        }
        if(interestRate<0.1 || interestRate>0.5){
            throw new InvalidLoanInterestRateException("Interest rate must be between 0.1 and 0.5");
        }

        // create installments
        Double totalAmount = amount*(1+interestRate);
        Double installmentAmount = totalAmount/numberOfInstallments;

        Loan loan = new Loan();
        loan.setCustomer(customer);
        loan.setLoanAmount(amount);
        loan.setNumberOfInstallments(numberOfInstallments);
        loan.setPaid(false);
        loan.setCreateDate(LocalDate.now().toString());
        loan.setInterestRate(interestRate);

        Loan savedLoan = loanRepository.save(loan);

        List<LoanInstallment> installments = new ArrayList<>();

        for(int i=0; i<numberOfInstallments; i++){
            LoanInstallment installment = new LoanInstallment();
            installment.setLoan(savedLoan); //associate installment with loan
            installment.setAmount(installmentAmount);
            installment.setPaidAmount(0.0);
            installment.setDueDate(LocalDate.now().plusMonths(i + 1).withDayOfMonth(1)); // due date increments month by month
            installment.setPaid(false);
            installments.add(installment);
        }
        loanInstallmentRepository.saveAll(installments);

        // Update the used credit limit for the customer
        customer.setUsedCreditLimit(newUsedLimit);
        customerRepository.save(customer);

        return savedLoan;
    }

    public List<LoanResponse> listLoans(Long customerId) {
        return loanRepository.findByCustomerId(customerId).stream().map(loan -> new LoanResponse(loan.getId(), loan.getLoanAmount(),
                loan.getNumberOfInstallments(), loan.getPaid(),
                loan.getCreateDate(), loan.getInterestRate())).toList();
    }

    public List<LoanInstallmentResponse> listLoanInstallments(Long loanId) {
        return loanInstallmentRepository.findByLoanIdOrderByDueDate(loanId).stream().map(li -> new LoanInstallmentResponse(li.getAmount(), li.getPaidAmount(), li.getDueDate(),
                li.getPaymentDate(), li.getPaid())).toList();
    }

    public void payLoan(Long loanId, Double amountPaid){
        List<LoanInstallment> installments = new ArrayList<>(loanInstallmentRepository.findByLoanIdAndIsPaid(loanId, false));
        if(installments.isEmpty()){
            throw new LoanAlreadyPaidException("Loan is already paid.");
        }


        //loan should be paid starting from first installment to last one - sort through date
        installments.sort(Comparator.comparing(LoanInstallment::getDueDate));

        LocalDate now = LocalDate.now();
        LocalDate maxAllowedPaymentDate = now.plusMonths(3).withDayOfMonth(1);

        double remainingAmount = amountPaid;

        for(LoanInstallment installment : installments){
            if(remainingAmount<=0) break;

            if (installment.getDueDate() == null) {
                throw new IllegalStateException("Installment due date cannot be null.");
            }

            //-	Installments have due date that still more than 3 calendar months cannot be paid
            if (installment.getDueDate().isAfter(maxAllowedPaymentDate)) {
                throw new InvalidPaymentDateRangeException("You cannot pay installments more than 3 months in advance.");
            }

            Double remainingInstallmentAmount = installment.getAmount()-installment.getPaidAmount();

            if(remainingAmount>=remainingInstallmentAmount){
                installment.setPaidAmount(installment.getAmount());
                installment.setPaid(true);
                remainingAmount -= remainingInstallmentAmount;
            } else{
                throw new PartialPaymentException("Partial payments not allowed. You must pay full installment amounts.");
            }
            loanInstallmentRepository.saveAll(installments); //save updated installments

            boolean allPaid = loanInstallmentRepository.findByLoanIdAndIsPaid(loanId, false).isEmpty();

            if(allPaid){
                Loan loan = loanRepository.findById(loanId).orElseThrow(() -> new RuntimeException("Loan not found"));
                loan.setPaid(true);
                loanRepository.save(loan);
            }

            //Update customers loan limit
            Loan loan = loanRepository.findById(loanId).orElseThrow(()->new RuntimeException("Loan not found"));
            Customer customer = customerRepository.findById(loan.getCustomer().getId()).orElseThrow(()->new RuntimeException("Customer not found"));
            Double updatedCreditLimit = customer.getUsedCreditLimit()-amountPaid;
            customer.setUsedCreditLimit(Math.max(updatedCreditLimit, 0));
            customerRepository.save(customer);
        }
    }
}
