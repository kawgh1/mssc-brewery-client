package com.kwgdev.msscbreweryclient.web.client;

import com.kwgdev.msscbreweryclient.web.model.CustomerDto;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.UUID;

/**
 * created by kw on 12/22/2020 @ 2:54 AM
 */

@ConfigurationProperties(prefix = "sfg.brewery", ignoreUnknownFields = false)
@Component
public class CustomerClient {

    public final String CUSTOMER_PATH_V1 = "/api/v1/customer/";
    private String apihost;

    private final RestTemplate restTemplate;

    // inject a RestTemplate Builder to create the rest Template
    public CustomerClient(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    // client methods

    // HTTP GET
    public CustomerDto getCustomerById(UUID uuid) {
        // return a customerDto object when we send a UUID to path "http://localhost:8080/api/v1/customer"
        return restTemplate.getForObject(apihost + CUSTOMER_PATH_V1 + uuid.toString(), CustomerDto.class);
    }

    // HTTP POST
    public URI saveNewCustomer(CustomerDto customerDto) {
        return restTemplate.postForLocation(apihost + CUSTOMER_PATH_V1, customerDto);
    }

    // HTTP PUT
    public void updateCustomer(UUID uuid, CustomerDto customerDto) {
        restTemplate.put(apihost + CUSTOMER_PATH_V1 + "/" + uuid.toString(), customerDto);
    }

    // HTTP DELETE
    public void deleteCustomer(UUID uuid) {
        restTemplate.delete(apihost + CUSTOMER_PATH_V1 + "/" +uuid.toString());
    }

    public void setApihost(String apihost) {
        this.apihost = apihost;
    }
}
