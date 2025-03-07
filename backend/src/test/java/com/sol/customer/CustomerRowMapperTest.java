package com.sol.customer;


import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CustomerRowMapperTest {


    @Test
    void mapRow() throws SQLException {
        //Given
        CustomerRowMapper customerRowMapper = new CustomerRowMapper();
        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.getLong("id")).thenReturn(1L);
        when(resultSet.getInt("age")).thenReturn(19);
        when(resultSet.getString("name"))
                .thenReturn("Mila");
        when(resultSet.getString("email"))
                .thenReturn("mila@gmail.com");
        when(resultSet.getString("gender")).thenReturn("FEMALE");
        //When
        Customer actual = customerRowMapper.mapRow(resultSet, 1);
        //Then
        Customer expected = new Customer(1L,
                "Mila",
                "mila@gmail.com",
                19,
                Gender.FEMALE);
        assertThat(actual).isEqualTo(expected);
    }
}