package com.aidevops.gateway.dto;

public record ChatResponse(
        String requestId,
        String userId,
        String message,
        String provider,
        long latencyMs
) {
}
