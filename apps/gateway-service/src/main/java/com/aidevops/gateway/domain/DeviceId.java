package com.aidevops.gateway.domain;

public record DeviceId(String value) {

    public DeviceId {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("deviceId is required");
        }
        value = value.trim();
    }
}
