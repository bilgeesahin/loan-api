package com.trial.loan_api.service;

import com.trial.loan_api.dto.request.RegisterRequest;
import com.trial.loan_api.model.Customer;
import com.trial.loan_api.model.Role;
import com.trial.loan_api.model.BankUser;
import com.trial.loan_api.repository.CustomerRepository;
import com.trial.loan_api.repository.RoleRepository;
import com.trial.loan_api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public BankUser createUser(RegisterRequest request) {

        BankUser bankUser = new BankUser();
        bankUser.setUsername(request.getUsername());  // Corrected the method call
        bankUser.setPassword(passwordEncoder.encode(request.getPassword()));
        Role role = roleRepository.findByName(Role.RoleType.CUSTOMER)
                .orElseThrow(() -> new RuntimeException("Role not found"));
        bankUser.setRoles(Set.of(role));
        userRepository.save(bankUser);

        Customer customer = new Customer();
        customer.setUser(bankUser);
        customer.setName(request.getName());
        customer.setSurname(request.getSurname());
        customer.setCreditLimit(request.getCreditLimit());
        customer.setUsedCreditLimit(request.getUsedCreditLimit());
        customerRepository.save(customer);

        return bankUser;
    }

}
