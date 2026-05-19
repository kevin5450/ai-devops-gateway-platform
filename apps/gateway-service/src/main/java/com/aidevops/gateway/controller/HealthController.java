package com.aidevops.gateway.controller;

import com.aidevops.gateway.dto.HealthResponse;
import java.time.Instant;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping("/health")
    public HealthResponse health() {
        return new HealthResponse(
                "UP",
                "gateway-service",
                Instant.now()
        );
    }
}
