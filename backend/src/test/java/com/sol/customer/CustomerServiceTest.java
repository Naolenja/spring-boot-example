package com.sol.customer;

import com.sol.exception.DuplicateResourceException;
import com.sol.exception.RequestValidationException;
import com.sol.exception.ResourceNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {
    private CustomerService underTest;
    @Mock
    private CustomerDao customerDao;

    @BeforeEach
    void setUp() {
        underTest = new CustomerService(customerDao);
    }

    @Test
    void getAllCustomers() {
        //When
        underTest.getAllCustomers();
        // Then
        verify(customerDao).selectAllCustomers();
    }

    @Test
    void canGetCustomer() {
        //Given
        var id = 1L;
        Customer customer =
                new Customer(id,
                        "Xenofont",
                        "xeno@font",
                        88,
                        Gender.MALE);
        when(customerDao.selectCustomerById(id))
                .thenReturn(Optional.of(customer));
        //When
        Customer actual = underTest.getCustomer(id);
        //Then
        assertThat(actual).isEqualTo(customer);
    }
    @Test
    void willThrowWhenGetCustomerReturnsEmptyOptional() {
        //Given
        var id = 1L;
        Customer customer = new Customer(id,
                "Xenofont",
                "xeno@font",
                88,
                Gender.MALE);
        when(customerDao.selectCustomerById(id))
                .thenReturn(Optional.empty());
        //When
        //Then
        assertThatThrownBy(()->underTest.getCustomer(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(
                        "Customer with id [%s] not found".formatted(id)
                );
    }

    @Test
    void addCustomer() {
        //Given
        String email = "Xeno@font.org";
        when(customerDao.existsPersonWithEmail(email)).thenReturn(false);
        CustomerRegistrationRequest request
                = new CustomerRegistrationRequest(
                        "Fontebleau", email, 834, Gender.randomGender()
        );
        //When
        underTest.addCustomer(request);
        //Then
        ArgumentCaptor<Customer> customerArgumentCaptor
                = ArgumentCaptor.forClass(Customer.class);
        verify(customerDao)
                .insertCustomer(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();
        assertThat(capturedCustomer.getId()).isNull();
        assertThat(capturedCustomer.getName()).isEqualTo(request.name());
        assertThat(capturedCustomer.getEmail()).isEqualTo(request.email());
        assertThat(capturedCustomer.getAge()).isEqualTo(request.age());
        assertThat(capturedCustomer.getGender()).isEqualTo(request.gender());

    }
    @Test
    void willThrowIFEmailExistsWhileAddingCustomer() {
        //Given
        String email = "Xeno@font.org";
        when(customerDao.existsPersonWithEmail(email)).thenReturn(true);
        CustomerRegistrationRequest request
                = new CustomerRegistrationRequest(
                "Fontebleau", email, 834, Gender.randomGender()
        );
        //When
        assertThatThrownBy(()->underTest.addCustomer(request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("Email already taken.");
        //Then

        verify(customerDao, never()).insertCustomer(any());


    }

    @Test
    void deleteCustomerById() {
        //Given
        Long customerId = 1L;
        //When
        underTest.deleteCustomerById(customerId);
        //Then
        verify(customerDao)
                .deleteCustomer(customerId);
    }

    @Test
    void canUpdateCustomerEmail() {
        //Given
        Long id = 1L;
        String newEmail = "fontebleau@email.com";
        Customer customer =
                new Customer(id,
                        "Xenofont",
                        "xeno@font",
                        88,
                        Gender.MALE);
        CustomerUpdateRequest request
                = new CustomerUpdateRequest(
                        null, newEmail, null, null
        );
        when(customerDao.selectCustomerById(id))
                .thenReturn(Optional.of(customer));
        when(customerDao.existsPersonWithEmail(newEmail))
                .thenReturn(false);
        //When
        underTest.updateCustomer(id,request);
        //Then
        ArgumentCaptor<Customer> customerArgumentCaptor
                = ArgumentCaptor.forClass(Customer.class);
        verify(customerDao).updateCustomer(
                customerArgumentCaptor.capture()
        );
        Customer capturedCustomer = customerArgumentCaptor.getValue();
        assertThat(capturedCustomer.getId())
                .isEqualTo(id);
        assertThat(capturedCustomer.getName())
                .isEqualTo(customer.getName());
        assertThat(capturedCustomer.getEmail())
                .isEqualTo(request.email());
        assertThat(capturedCustomer.getAge())
                .isEqualTo(customer.getAge());
        assertThat(capturedCustomer.getGender())
                .isEqualTo(customer.getGender());
    }
    @Test
    void canUpdateCustomerName() {
        //Given
        Long id = 1L;
        String newName = "Fontebleauom";
        Customer customer =
                new Customer(id,
                        "Xenofont",
                        "xeno@font",
                        88,
                        Gender.MALE);
        CustomerUpdateRequest request
                = new CustomerUpdateRequest(
                newName,null, null, null
        );
        when(customerDao.selectCustomerById(id))
                .thenReturn(Optional.of(customer));
        //When
        underTest.updateCustomer(id,request);
        //Then
        ArgumentCaptor<Customer> customerArgumentCaptor
                = ArgumentCaptor.forClass(Customer.class);
        verify(customerDao).updateCustomer(
                customerArgumentCaptor.capture()
        );
        Customer capturedCustomer = customerArgumentCaptor.getValue();
        assertThat(capturedCustomer.getId())
                .isEqualTo(id);
        assertThat(capturedCustomer.getName())
                .isEqualTo(request.name());
        assertThat(capturedCustomer.getEmail())
                .isEqualTo(customer.getEmail());
        assertThat(capturedCustomer.getAge())
                .isEqualTo(customer.getAge());
        assertThat(capturedCustomer.getGender())
                .isEqualTo(customer.getGender());
    }
    @Test
    void canUpdateCustomerAge() {
        //Given
        Long id = 1L;
        int newAge = 12;
        Customer customer =
                new Customer(id,
                        "Xenofont",
                        "xeno@font",
                        88,
                        Gender.MALE);
        CustomerUpdateRequest request
                = new CustomerUpdateRequest(
                null, null, newAge, null
        );
        when(customerDao.selectCustomerById(id))
                .thenReturn(Optional.of(customer));
        //When
        underTest.updateCustomer(id,request);
        //Then
        ArgumentCaptor<Customer> customerArgumentCaptor
                = ArgumentCaptor.forClass(Customer.class);
        verify(customerDao).updateCustomer(
                customerArgumentCaptor.capture()
        );
        Customer capturedCustomer = customerArgumentCaptor.getValue();
        assertThat(capturedCustomer.getId())
                .isEqualTo(id);
        assertThat(capturedCustomer.getName())
                .isEqualTo(customer.getName());
        assertThat(capturedCustomer.getEmail())
                .isEqualTo(customer.getEmail());
        assertThat(capturedCustomer.getAge())
                .isEqualTo(request.age());
        assertThat(capturedCustomer.getGender())
                .isEqualTo(customer.getGender());
    }
    @Test
    void canUpdateCustomerGender() {
        //Given
        Long id = 1L;
        Gender newGender = Gender.randomGender();
        Customer customer =
                new Customer(id,
                        "Xenofont",
                        "xeno@font",
                        88,
                        Gender.MALE);
        CustomerUpdateRequest request
                = new CustomerUpdateRequest(
                null, null, null, newGender
        );
        when(customerDao.selectCustomerById(id))
                .thenReturn(Optional.of(customer));
        //When
        underTest.updateCustomer(id,request);
        //Then
        ArgumentCaptor<Customer> customerArgumentCaptor
                = ArgumentCaptor.forClass(Customer.class);
        verify(customerDao).updateCustomer(
                customerArgumentCaptor.capture()
        );
        Customer capturedCustomer = customerArgumentCaptor.getValue();
        assertThat(capturedCustomer.getId())
                .isEqualTo(id);
        assertThat(capturedCustomer.getName())
                .isEqualTo(customer.getName());
        assertThat(capturedCustomer.getEmail())
                .isEqualTo(customer.getEmail());
        assertThat(capturedCustomer.getAge())
                .isEqualTo(customer.getAge());
        assertThat(capturedCustomer.getGender())
                .isEqualTo(request.gender());
    }
    @Test
    void willThrowIFEmailExistsWhileUpdatingCustomer() {
        //Given
        Long id = 1L;
        Customer customer =
                new Customer(id,
                        "Xenofont",
                        "xeno@font",
                        88,
                        Gender.MALE);
        when(customerDao.selectCustomerById(id))
                .thenReturn(Optional.of(customer));
        String email = "Xeno@font.org";
        when(customerDao.existsPersonWithEmail(email)).thenReturn(true);
        CustomerUpdateRequest request
                = new CustomerUpdateRequest(
                null, email, null, null
        );
        //When
        assertThatThrownBy(()->underTest.updateCustomer(id, request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("Email already taken.");
        //Then
        verify(customerDao, never()).updateCustomer(any());


    }
    @Test
    void willThrowIfNoChangesWhenUpdatingCustomer() {
        //Given
        Long id = 1L;
        String newName = "Xenofont";
        String newEmail = "xeno@font";
        int newAge = 12;
        Gender newGender = Gender.randomGender();
        Customer customer =
                new Customer(id,
                        newName,
                        newEmail,
                        newAge,
                        newGender);
        CustomerUpdateRequest request
                = new CustomerUpdateRequest(
                newName, newEmail, newAge, newGender
        );
        when(customerDao.selectCustomerById(id))
                .thenReturn(Optional.of(customer));
        //When
        assertThatThrownBy(()->underTest.updateCustomer(id, request))
                .isInstanceOf(RequestValidationException.class)
                .hasMessage("no data changes found");
        //Then
        verify(customerDao, never()).updateCustomer(any());
    }
}
