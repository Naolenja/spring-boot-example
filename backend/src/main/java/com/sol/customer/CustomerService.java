package com.sol.customer;

import com.sol.exception.DuplicateResourceException;
import com.sol.exception.RequestValidationException;
import com.sol.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class CustomerService {
    private final CustomerDao customerDao;

    public CustomerService(@Qualifier("jdbc") CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    public List<Customer> getAllCustomers(){
        return customerDao.selectAllCustomers();
    }
    public Customer getCustomer(Long id){
        return customerDao.selectCustomerById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Customer with id [%s] not found".formatted(id)
                ));
    }
    public void addCustomer(CustomerRegistrationRequest customerRegistrationRequest){
        //check if email exists
        String email = customerRegistrationRequest.email();
        if (customerDao.existsPersonWithEmail(email)){
            throw new DuplicateResourceException("Email already taken.");
        }
        //add
        customerDao.insertCustomer(new Customer(
                customerRegistrationRequest.name(),
                customerRegistrationRequest.email(),
                customerRegistrationRequest.age(),
                customerRegistrationRequest.gender()
        ));
    }
    public void deleteCustomerById(Long customerId){
        customerDao.deleteCustomer(customerId);
    }
    public void updateCustomer(
            Long id,
            CustomerUpdateRequest customerUpdateRequest
    ){
        Customer customer = getCustomer(id);
        boolean changes = false;
        if (customerUpdateRequest.name() !=null && !customerUpdateRequest.name().equals(customer.getName())){
            customer.setName(customerUpdateRequest.name());
            changes = true;
        }
        if (customerUpdateRequest.email() !=null && !customerUpdateRequest.email().equals(customer.getEmail())){
            if (customerDao.existsPersonWithEmail(customerUpdateRequest.email())){
                throw new DuplicateResourceException("Email already taken.");
            }
            customer.setEmail(customerUpdateRequest.email());
            changes = true;
        }
        if (customerUpdateRequest.age() !=null && !customerUpdateRequest.age().equals(customer.getAge())){
            customer.setAge(customerUpdateRequest.age());
            changes = true;
        }

        if (customerUpdateRequest.gender() !=null && !customerUpdateRequest.gender().equals(customer.getGender())){
            customer.setGender(customerUpdateRequest.gender());
            changes = true;
        }

        if(!changes){
            throw new RequestValidationException("no data changes found");
        }
        customerDao.updateCustomer(customer);
    }
}
