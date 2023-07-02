package com.bookmyticket.entity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class MyInterceptor implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {

        log.info("Request URI: {}", request.getURI());
        log.info("Request method: {}", request.getMethod());
        log.info("Request headers: {}", request.getHeaders());
        log.info("Request body: {}", new String(body, StandardCharsets.UTF_8));

        ClientHttpResponse response = execution.execute(request, body);

        log.info("Response status code: {}", response.getStatusCode());
        log.info("Response headers: {}", response.getHeaders());

        //Read the response body from the input stream
        InputStream inputStream = response.getBody();
        String responseBody = new BufferedReader(new InputStreamReader(inputStream)).lines().collect(Collectors.joining("\n"));
        log.info("Response body: {}", responseBody);

        return response;
    }
}

@Configuration
class MyConfig {

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setInterceptors(Collections.singletonList(new MyInterceptor()));
        return restTemplate;
    }
}