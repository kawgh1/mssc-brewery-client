package com.kwgdev.msscbreweryclient.web.client;

/**
 * created by kw on 12/22/2020 @ 3:04 AM
 */
import com.kwgdev.msscbreweryclient.web.model.CustomerDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.URI;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CustomerClientTest {

    @Autowired
    CustomerClient client;

    // these tests require the Brewery Microservice to be running
    @Test
    void getCustomerByIdTest() {
        CustomerDto dto = client.getCustomerById(UUID.randomUUID());

        assertNotNull(dto);

    }

    @Test
    void testSaveNewCustomer() {
        // given
        CustomerDto customerDto = CustomerDto.builder().name("Jim").build();

        URI uri = client.saveNewCustomer(customerDto);

        assertNotNull(uri);

        System.out.println(uri.toString());
    }

    @Test
    void testUpdateCustomer() {
        // given
        CustomerDto customerDto = CustomerDto.builder().name("John").build();

        client.updateCustomer(UUID.randomUUID(), customerDto);
    }

    @Test
    void testDeleteCustomer() {

        client.deleteCustomer(UUID.randomUUID());
    }
}
