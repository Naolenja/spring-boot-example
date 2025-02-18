package com.sol.journey;

import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import com.sol.customer.Customer;
import com.sol.customer.CustomerRegistrationRequest;
import com.sol.customer.CustomerUpdateRequest;
import com.sol.customer.Gender;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public class CustomerIT {
    @Autowired
    private WebTestClient webTestClient;
    private static final Random RANDOM = new Random();
    private static final String CUSTOMER_URI = "/api/v1/customers";

    @Test
    void canRegisterCustomer() {
        //create registration request
        Faker faker = new Faker();
        Name fakeName = faker.name();
        String name = fakeName.fullName();
        String email
                = fakeName.lastName()
                + UUID.randomUUID()
                + "whatever.io";
        int age = RANDOM.nextInt(0,120);
        Gender gender = Gender.randomGender();
        CustomerRegistrationRequest request
                = new CustomerRegistrationRequest(name, email, age, gender);
        // send a post request
        webTestClient.post()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        Mono.just(request),
                        CustomerRegistrationRequest.class
                )
                .exchange()
                .expectStatus()
                .isOk();
        // get all customers
        List<Customer> allCustomers = webTestClient.get()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {})
                .returnResult()
                .getResponseBody();
        //make sure, customer is present
        Customer expectedCustomer = new Customer(name, email, age, gender);
        assertThat(allCustomers)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id").contains(expectedCustomer);
        //get customer by id
        Long id = allCustomers.stream().filter(customer -> customer.getEmail().equals(email)).map(Customer::getId).findFirst().orElseThrow();
        expectedCustomer.setId(id);
        webTestClient.get()
                .uri(CUSTOMER_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<Customer>() {})
                .isEqualTo(expectedCustomer);
    }

    @Test
    void canDeleteCustomer() {
        //create registration request
        Faker faker = new Faker();
        Name fakeName = faker.name();
        String name = fakeName.fullName();
        String email
                = fakeName.lastName()
                + UUID.randomUUID()
                + "whatever.io";
        int age = RANDOM.nextInt(0,120);
        Gender gender = Gender.randomGender();
        CustomerRegistrationRequest request
                = new CustomerRegistrationRequest(name, email, age, gender);
        // send a post request
        webTestClient.post()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        Mono.just(request),
                        CustomerRegistrationRequest.class
                )
                .exchange()
                .expectStatus()
                .isOk();
        // get all customers
        List<Customer> allCustomers = webTestClient.get()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {})
                .returnResult()
                .getResponseBody();

        Long id = allCustomers.stream().filter(customer -> customer.getEmail().equals(email)).map(Customer::getId).findFirst().orElseThrow();
        //delete customer
        webTestClient.delete()
                .uri(CUSTOMER_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk();
        //get customer by id
        webTestClient.get()
                .uri(CUSTOMER_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isNotFound();
    }
    @Test
    void canUpdateCustomer() {
        //create registration request
        Faker faker = new Faker();
        Name fakeName = faker.name();
        String name = fakeName.fullName();
        String email
                = fakeName.lastName()
                + UUID.randomUUID()
                + "whatever.io";
        int age = RANDOM.nextInt(0,120);
        Gender gender = Gender.randomGender();
        CustomerRegistrationRequest request
                = new CustomerRegistrationRequest(name, email, age, gender);
        // send a post request
        webTestClient.post()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        Mono.just(request),
                        CustomerRegistrationRequest.class
                )
                .exchange()
                .expectStatus()
                .isOk();
        // get all customers
        List<Customer> allCustomers = webTestClient.get()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {})
                .returnResult()
                .getResponseBody();
        //get customer's id
        Long id = allCustomers.stream().filter(customer -> customer.getEmail().equals(email)).map(Customer::getId).findFirst().orElseThrow();
        //create update request
        Name fakeUpdateName = faker.name();
        String updateName = fakeUpdateName.fullName();
        String updateEmail
                = fakeUpdateName.lastName()
                + UUID.randomUUID()
                + "updated.io";
        int updateAge = RANDOM.nextInt(0,120);
        Gender updateGender = Gender.randomGender();
        CustomerUpdateRequest updateRequest
                = new CustomerUpdateRequest(updateName, updateEmail, updateAge, updateGender);
        //send a put request
        webTestClient.put()
                .uri(CUSTOMER_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        Mono.just(updateRequest),
                        CustomerUpdateRequest.class
                )
                .exchange()
                .expectStatus()
                .isOk();
        //get customer by id
        Customer expectedCustomer = new Customer(updateName, updateEmail, updateAge, updateGender);
        expectedCustomer.setId(id);
        webTestClient.get()
                .uri(CUSTOMER_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<Customer>() {})
                .isEqualTo(expectedCustomer);
    }
}
