package com.sol.customer;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Repository("list")
public class CustomerListDataAccessService implements CustomerDao{
    public static List<Customer> customers;
    static {
        customers = new ArrayList<>();
        Customer alex = new Customer(1L, "Alex", "alex@gmail.com", 21);
        customers.add(alex);
        Customer lex = new Customer(2L, "Lex", "lex@gmail.com", 19);
        customers.add(lex);
    }
    @Override
    public List<Customer> selectAllCustomers(){
        return customers;
    }

    @Override
    public Optional<Customer> selectCustomerById(Long id) {
        return customers.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst();

    }

    @Override
    public void insertCustomer(Customer customer) {
        customers.add(customer);
    }

    @Override
    public void updateCustomer(Customer customer) {
        customers.add(customer);
    }

    @Override
    public boolean existsPersonWithEmail(String email) {
        return customers.stream().anyMatch(c -> c.getEmail().equals(email));
    }

    @Override
    public void deleteCustomer(Long id) {
        customers.removeIf(c -> c.getId().equals(id));
    }
}
