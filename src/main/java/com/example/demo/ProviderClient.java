package com.example.demo;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "provider-service") // Discovered via Zookeeper
public interface ProviderClient {
    @GetMapping("/data")
    String getData();
}
