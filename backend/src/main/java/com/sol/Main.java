package com.sol;

import com.github.javafaker.Faker;
import com.sol.customer.Customer;
import com.sol.customer.CustomerRepository;
import com.sol.customer.Gender;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import java.util.List;
import java.util.Random;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
    @Bean
    CommandLineRunner runner (CustomerRepository customerRepository){
        return args -> {
            Faker faker = new Faker();
            Random random = new Random();
            String name = faker.name().fullName();
            String name1 = faker.name().fullName();
            Customer alex = new Customer(name,
                    name.toLowerCase().replace(" ", ".") + "@acdc.com", random.nextInt(16,99), Gender.MALE);
            Customer lex = new Customer(name1,
                    name1.toLowerCase().replace(" ", ".") + "@gmail.com", 19, Gender.FEMALE);
            List<Customer> customers= List.of(alex, lex);
            customerRepository.saveAll(customers);
        };
    }
}
