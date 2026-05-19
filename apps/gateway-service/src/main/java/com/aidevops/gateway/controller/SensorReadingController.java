package com.aidevops.gateway.controller;

import com.aidevops.gateway.domain.SensorReading;
import com.aidevops.gateway.dto.SensorIssueResponse;
import com.aidevops.gateway.dto.SensorReadingRequest;
import com.aidevops.gateway.dto.SensorReadingResponse;
import com.aidevops.gateway.service.SensorReadingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SensorReadingController {

    private final SensorReadingService sensorReadingService;

    public SensorReadingController(SensorReadingService sensorReadingService) {
        this.sensorReadingService = sensorReadingService;
    }

    @PostMapping("/api/readings")
    @ResponseStatus(HttpStatus.CREATED)
    public SensorReadingResponse create(@Valid @RequestBody SensorReadingRequest request) {
        SensorReading reading = sensorReadingService.create(request);
        return SensorReadingResponse.accepted(reading);
    }

    @GetMapping("/api/devices/{deviceId}/latest")
    public SensorReadingResponse latest(@PathVariable String deviceId) {
        return SensorReadingResponse.latest(sensorReadingService.getLatest(deviceId));
    }

    @GetMapping("/api/devices/{deviceId}/issues/latest")
    public SensorIssueResponse latestIssue(@PathVariable String deviceId) {
        return sensorReadingService.getLatestIssue(deviceId);
    }
}
