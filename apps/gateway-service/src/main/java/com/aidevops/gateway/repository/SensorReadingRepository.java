package com.aidevops.gateway.repository;

import com.aidevops.gateway.domain.DeviceId;
import com.aidevops.gateway.domain.SensorReading;
import java.util.Optional;

public interface SensorReadingRepository {

    SensorReading save(SensorReading reading);

    Optional<SensorReading> findLatest(DeviceId deviceId);
}
