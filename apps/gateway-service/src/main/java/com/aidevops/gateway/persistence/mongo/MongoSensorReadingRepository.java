package com.aidevops.gateway.persistence.mongo;

import com.aidevops.gateway.domain.DeviceId;
import com.aidevops.gateway.domain.SensorReading;
import com.aidevops.gateway.repository.SensorReadingRepository;
import java.util.Optional;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

@Repository
@Profile("mongo")
public class MongoSensorReadingRepository implements SensorReadingRepository {

    private final MongoSensorReadingSpringRepository springRepository;

    public MongoSensorReadingRepository(MongoSensorReadingSpringRepository springRepository) {
        this.springRepository = springRepository;
    }

    @Override
    public SensorReading save(SensorReading reading) {
        SensorReadingDocument saved = springRepository.save(SensorReadingMapper.toDocument(reading));
        return SensorReadingMapper.toDomain(saved);
    }

    @Override
    public Optional<SensorReading> findLatest(DeviceId deviceId) {
        return springRepository.findTopByDeviceIdOrderByMeasuredAtDesc(deviceId.value())
                .map(SensorReadingMapper::toDomain);
    }
}
