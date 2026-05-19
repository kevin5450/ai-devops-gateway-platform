package com.aidevops.gateway.service;

import com.aidevops.gateway.domain.DeviceId;
import com.aidevops.gateway.domain.SensorReading;
import com.aidevops.gateway.dto.SensorIssueResponse;
import com.aidevops.gateway.dto.SensorReadingRequest;
import com.aidevops.gateway.exception.ResourceNotFoundException;
import com.aidevops.gateway.repository.SensorReadingRepository;
import org.springframework.stereotype.Service;

@Service
public class SensorReadingService {

    private final SensorReadingRepository repository;
    private final SensorThresholdPolicy thresholdPolicy;

    public SensorReadingService(SensorReadingRepository repository, SensorThresholdPolicy thresholdPolicy) {
        this.repository = repository;
        this.thresholdPolicy = thresholdPolicy;
    }

    public SensorReading create(SensorReadingRequest request) {
        SensorReading reading = new SensorReading(
                new DeviceId(request.deviceId()),
                request.measuredAt(),
                request.temperature(),
                request.humidity(),
                request.light()
        );

        return repository.save(reading);
    }

    public SensorReading getLatest(String deviceId) {
        DeviceId id = new DeviceId(deviceId);
        return repository.findLatest(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No sensor reading found for deviceId: " + id.value()
                ));
    }

    public SensorIssueResponse getLatestIssue(String deviceId) {
        SensorReading reading = getLatest(deviceId);
        return new SensorIssueResponse(
                reading.deviceId().value(),
                reading.measuredAt(),
                thresholdPolicy.evaluateTemperature(reading.temperature()),
                thresholdPolicy.evaluateHumidity(reading.humidity())
        );
    }
}
