package com.sol.customer;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("jdbc")
public class CustomerJDBCDataAccessService implements CustomerDao{
    private final JdbcTemplate jdbcTemplate;
    private final CustomerRowMapper customerRowMapper;

    public CustomerJDBCDataAccessService(JdbcTemplate jdbcTemplate, CustomerRowMapper customerRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.customerRowMapper = customerRowMapper;
    }

    @Override
    public List<Customer> selectAllCustomers() {
        var sql = """
        SELECT id, name, email, age, gender
        FROM customer
        """;
        return jdbcTemplate.query(sql, customerRowMapper);
    }

    @Override
    public Optional<Customer> selectCustomerById(Long id) {
        var sql = """
        SELECT id, name, email, age, gender
        FROM customer
        WHERE id = ?
        """;
        return jdbcTemplate.query(sql, customerRowMapper, id).stream().findFirst();
    }

    @Override
    public void insertCustomer(Customer customer) {
        var sql = """
                INSERT INTO customer(name, email, age, gender)
                VALUES (?, ?, ?, ?)
                """;
        int result = jdbcTemplate.update(
                sql,
                customer.getName(),
                customer.getEmail(),
                customer.getAge(),
                customer.getGender().name());
        System.out.println("jdbcTemplate.update = " + result);
    }

    @Override
    public void updateCustomer(Customer customer) {
        if(customer.getName() !=null){
            var sql = """
                    UPDATE customer
                    SET name = ?
                    WHERE id = ?
                    """;
            int result = jdbcTemplate.update(
                    sql,
                    customer.getName(),
                    customer.getId()
            );
            System.out.println("update customer by id result = " + result);
        }
        if(customer.getEmail() !=null){
            var sql = """
                    UPDATE customer
                    SET email = ?
                    WHERE id = ?
                    """;
            int result = jdbcTemplate.update(
                    sql,
                    customer.getEmail(),
                    customer.getId()
            );
            System.out.println("update customer by id result = " + result);
        }
        if(customer.getAge() !=null){
            var sql = """
                    UPDATE customer
                    SET age = ?
                    WHERE id = ?
                    """;
            int result = jdbcTemplate.update(
                    sql,
                    customer.getAge(),
                    customer.getId()
            );
            System.out.println("update customer by id result = " + result);
        }
        if(customer.getGender() !=null){
            var sql = """
                    UPDATE customer
                    SET gender = ?
                    WHERE id = ?
                    """;
            int result = jdbcTemplate.update(
                    sql,
                    customer.getGender().name(),
                    customer.getId()
            );
            System.out.println("update customer by id result = " + result);
        }
    }

    @Override
    public boolean existsPersonWithEmail(String email) {
        //my first solution
//        var sql = """
//        SELECT id, name, email, age
//        FROM customer
//        WHERE email = ?
//        """;
//        return jdbcTemplate.query(sql, customerRowMapper, email).stream().anyMatch(c->true);
        var sql = """
                SELECT count(id)
                FROM customer
                WHERE email = ?
                """;
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, email);
        return count != null && count > 0;
    }

    @Override
    public void deleteCustomer(Long id) {
        var sql = """
        DELETE
        FROM customer
        WHERE id = ?
        """;
        int result = jdbcTemplate.update(sql, id);
        System.out.println("delete customer by id result" + result);
    }
}
