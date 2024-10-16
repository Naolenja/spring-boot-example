package com.sol.customer;

import org.hibernate.sql.Update;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("api/v1/customers")
public class CustomerController {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public List<Customer> getCustomers(){
        return customerService.getAllCustomers();
    }
    @GetMapping("{customerId}")
    public Customer getCustomerById(@PathVariable("customerId") Long customerId){
        return customerService.getCustomer(customerId);
    }
    @PostMapping
    public void registerCustomer(@RequestBody CustomerRegistrationRequest request){
        customerService.addCustomer(request);
    }
    @DeleteMapping("{customerId}")
    public void deleteCustomer(@PathVariable("customerId") Long customerId){
        customerService.deleteCustomerById(customerId);
    }
    @PutMapping("{customerId}")
    public void changeCustomerDetails(@PathVariable("customerId") Long customerId, @RequestBody CustomerUpdateRequest request){
        customerService.updateCustomer(customerId, request);
    }
}
