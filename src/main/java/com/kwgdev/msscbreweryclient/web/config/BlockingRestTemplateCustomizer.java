package com.kwgdev.msscbreweryclient.web.config;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateCustomizer;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * created by kw on 12/22/2020 @ 3:48 AM
 */

/*    If you were doing a lot of volume - high traffic - having this request pull would be much faster
      - In both cases (Blocking and Non-Blocking (NIO)) what you will see with these implementations
        is you will set up a Connection Manager, a Request Configuration,
        then set up the client - the client will get injected into the Request Factory
        and that Request Factory is going to be set on the REST Template

        * Very Important to set this up *

        This will really help performance if you're going to have a lot of RESTful Web Service traffic

*/


@Component
public class BlockingRestTemplateCustomizer implements RestTemplateCustomizer {

//    Properties
    private final Integer maxTotalConnections;
    private final Integer defaultMaxTotalConnections;
    private final Integer connectionRequestTimeout;
    private final Integer socketTimeout;

    // constructor
    public BlockingRestTemplateCustomizer(
            @Value("${sfg.maxtotalconnections}") Integer maxTotalConnections,
            @Value("${sfg.defaultmaxtotalconnections}") Integer defaultMaxTotalConnections,
            @Value("${sfg.connectionrequesttimeout}") Integer connectionRequestTimeout,
            @Value("${sfg.sockettimeout}") Integer socketTimeout) {
        this.maxTotalConnections = maxTotalConnections;
        this.defaultMaxTotalConnections = defaultMaxTotalConnections;
        this.connectionRequestTimeout = connectionRequestTimeout;
        this.socketTimeout = socketTimeout;
    }

    // set up Apache specific stuff
    public ClientHttpRequestFactory clientHttpRequestFactory(){
        // from the Apache library
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        // set max connections
//        connectionManager.setMaxTotal(100);
        connectionManager.setMaxTotal(maxTotalConnections);
        // max connections per route
//        connectionManager.setDefaultMaxPerRoute(20);
        connectionManager.setDefaultMaxPerRoute(defaultMaxTotalConnections);

        // set timeout - if a request takes longer than 3 seconds it will error and fail
        RequestConfig requestConfig = RequestConfig
                .custom()
//                .setConnectionRequestTimeout(3000)
                .setConnectionRequestTimeout(connectionRequestTimeout)
//                .setSocketTimeout(3000)
                .setSocketTimeout(socketTimeout)
                .build();

        CloseableHttpClient httpClient = HttpClients
                .custom()
                .setConnectionManager(connectionManager)
                .setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy())
                .setDefaultRequestConfig(requestConfig)
                .build();

        // return instance of the Spring implementation of the ClientHTTPRequestFactory
        return new HttpComponentsClientHttpRequestFactory(httpClient);
    }

    @Override
    public void customize(RestTemplate restTemplate) { // inject in restTemplate

        restTemplate.setRequestFactory(this.clientHttpRequestFactory()); // set up request factory for pooling connections
    }
}

