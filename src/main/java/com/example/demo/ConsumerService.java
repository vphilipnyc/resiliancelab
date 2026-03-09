package com.example.demo;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.stereotype.Service;

@Service
public class ConsumerService {
    private final ProviderClient providerClient;

    public ConsumerService(ProviderClient providerClient) {
        this.providerClient = providerClient;
    }

    @CircuitBreaker(name = "providerBreaker", fallbackMethod = "reliableResponse")
    public String getInfo() {
        return providerClient.getData();
    }

    // This is called when the circuit is OPEN or an error occurs
    public String reliableResponse(Exception e) {
        return "Fallback: Provider is currently down or slow.";
    }
}