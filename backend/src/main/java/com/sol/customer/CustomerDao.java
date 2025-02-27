package com.sol.customer;



import java.util.List;
import java.util.Optional;

public interface CustomerDao {
    List<Customer> selectAllCustomers();
    Optional<Customer> selectCustomerById(Long id);
    void insertCustomer(Customer customer);
    void updateCustomer(Customer customer);
    boolean existsPersonWithEmail(String email);
    void deleteCustomer(Long id);
}
