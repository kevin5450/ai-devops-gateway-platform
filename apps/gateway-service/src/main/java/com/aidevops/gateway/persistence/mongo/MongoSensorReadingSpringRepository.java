package com.aidevops.gateway.persistence.mongo;

import java.util.Optional;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.repository.MongoRepository;

@Profile("mongo")
public interface MongoSensorReadingSpringRepository extends MongoRepository<SensorReadingDocument, String> {

    Optional<SensorReadingDocument> findTopByDeviceIdOrderByMeasuredAtDesc(String deviceId);
}
