package com.aidevops.gateway.repository;

import com.aidevops.gateway.domain.DeviceId;
import com.aidevops.gateway.domain.SensorReading;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

@Repository
@Profile("!mongo")
public class InMemorySensorReadingStore implements SensorReadingRepository {

    private final ConcurrentMap<String, SensorReading> latestReadings = new ConcurrentHashMap<>();

    @Override
    public SensorReading save(SensorReading reading) {
        latestReadings.merge(
                reading.deviceId().value(),
                reading,
                (current, next) -> next.measuredAt().isAfter(current.measuredAt()) ? next : current
        );
        return latestReadings.get(reading.deviceId().value());
    }

    @Override
    public Optional<SensorReading> findLatest(DeviceId deviceId) {
        return Optional.ofNullable(latestReadings.get(deviceId.value()));
    }
}
