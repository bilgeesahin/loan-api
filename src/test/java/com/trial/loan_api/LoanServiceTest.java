package com.trial.loan_api;

import com.trial.loan_api.exception.*;
import com.trial.loan_api.model.Customer;
import com.trial.loan_api.model.Loan;
import com.trial.loan_api.model.LoanInstallment;
import com.trial.loan_api.repository.CustomerRepository;
import com.trial.loan_api.repository.LoanInstallmentRepository;
import com.trial.loan_api.repository.LoanRepository;
import com.trial.loan_api.service.LoanService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class LoanServiceTest {
    @InjectMocks
    private LoanService loanService;
    @Mock
    private LoanRepository loanRepository;
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private LoanInstallmentRepository loanInstallmentRepository;

    @Test
    public void testCreateLoanSuccessful(){
        Long customerId = 1L;
        double loanAmount = 10000;
        double interestRate = 0.2;
        int numberOfInstallments = 12;

        Customer customer = new Customer();
        customer.setId(customerId);
        customer.setCreditLimit(50000.0);
        customer.setUsedCreditLimit(5000.0);

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(loanRepository.save(any(Loan.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // WHEN
        Loan createdLoan = loanService.createLoan(customerId, loanAmount, interestRate, numberOfInstallments);

        // THEN
        assertNotNull(createdLoan);
        assertEquals(loanAmount, createdLoan.getLoanAmount(), 0.01);
        assertEquals(numberOfInstallments, createdLoan.getNumberOfInstallments());
        verify(loanRepository, times(1)).save(any(Loan.class));
    }

    @Test
    public void testExceedsCreditLimit() {
        // GIVEN
        Long customerId = 1L;
        double loanAmount = 50000; // exceed current limit
        double interestRate = 0.2;
        int numberOfInstallments = 12;

        Customer customer = new Customer();
        customer.setId(customerId);
        customer.setCreditLimit(40000.0);
        customer.setUsedCreditLimit(20000.0);

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));

        // WHEN & THEN
        Exception exception = assertThrows(InsufficientCreditLimitException.class, () -> {
            loanService.createLoan(customerId, loanAmount, interestRate, numberOfInstallments);
        });

        assertEquals("Customer does not have enough credit limit.", exception.getMessage());
    }

    @Test
    public void testInvalidInstallmentCount() {
        Long customerId = 1L;
        double loanAmount = 10000;
        double interestRate = 0.2;
        int numberOfInstallments = 8; //invalid

        Customer customer = new Customer();
        customer.setId(customerId);
        customer.setCreditLimit(50000.0);
        customer.setUsedCreditLimit(5000.0);

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));

        // WHEN & THEN
        Exception exception = assertThrows(InvalidLoanInstallmentException.class, () -> {
            loanService.createLoan(customerId, loanAmount, interestRate, numberOfInstallments);
        });

        assertEquals("Invalid number of installments. Must be 6, 9, 12, or 24.", exception.getMessage());
    }

    @Test
    public void testPartialPaymentNotAllowed() {
        Customer customer = new Customer();
        customer.setId(1L);

        Loan loan = new Loan();
        loan.setId(1L);
        loan.setCustomer(customer);
        loan.setPaid(false);

        LoanInstallment installment = new LoanInstallment();
        installment.setId(1L);

        installment.setLoan(loan);
        installment.setAmount(500.0);
        installment.setPaidAmount(0.0);
        installment.setDueDate(LocalDate.now().plusMonths(1));
        installment.setPaid(false);

        when(loanInstallmentRepository.findByLoanIdAndIsPaid(loan.getId(), false))
                .thenReturn(List.of(installment));

        Exception exception = assertThrows(PartialPaymentException.class, () -> {
            loanService.payLoan(loan.getId(), 200.0); // Partial payment
        });

        assertEquals("Partial payments not allowed. You must pay full installment amounts.", exception.getMessage());
    }

    @Test
    public void testCannotPayMoreThan3Months() {
        Customer customer = new Customer();
        customer.setId(1L);

        Long loanId = 1L;
        LoanInstallment installment = new LoanInstallment();
        installment.setId(1L);
        //    installment.setLoanId(loanId);
        installment.setAmount(500.0);
        installment.setPaidAmount(0.0);
        installment.setDueDate(LocalDate.now().plusMonths(4)); //date further than 3 months

        when(loanInstallmentRepository.findByLoanIdAndIsPaid(loanId, false))
                .thenReturn(List.of(installment));

        Exception exception = assertThrows(InvalidPaymentDateRangeException.class, () -> {
            loanService.payLoan(loanId, 500.0);
        });

        assertEquals("You cannot pay installments more than 3 months in advance.", exception.getMessage());
    }

    @Test
    public void testLoanMarkedAsPaidWhenAllInstallmentsPaid() {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setUsedCreditLimit(1000.0);

        Loan loan = new Loan();
        loan.setId(1L);
        loan.setCustomer(customer);
        loan.setPaid(false);


        LoanInstallment installment1 = new LoanInstallment();
        installment1.setId(1L);
        installment1.setLoan(loan);
        installment1.setAmount(500.0);
        installment1.setPaidAmount(0.0);
        installment1.setDueDate(LocalDate.now().plusMonths(1));
        installment1.setPaid(false);

        LoanInstallment installment2 = new LoanInstallment();
        installment2.setId(2L);
        installment2.setLoan(loan);
        installment2.setAmount(500.0);
        installment2.setPaidAmount(0.0);
        installment2.setDueDate(LocalDate.now().plusMonths(2));
        installment2.setPaid(false);

        when(customerRepository.findById(customer.getId())).thenReturn(Optional.of(customer));

        when(loanInstallmentRepository.findByLoanIdAndIsPaid(loan.getId(), false))
                .thenReturn(Arrays.asList(installment1, installment2))
                .thenReturn(List.of()); // should return empty list when all the installments paid

        when(loanRepository.findById(loan.getId())).thenReturn(Optional.of(loan));

        loanService.payLoan(loan.getId(), 500.0);
        assertThrows(LoanAlreadyPaidException.class, () -> {
            loanService.payLoan(loan.getId(), 500.0);
        });

        assertTrue(loan.getPaid());
        verify(loanRepository, times(1)).save(loan);
    }
}
