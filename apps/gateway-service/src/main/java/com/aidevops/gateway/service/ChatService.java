package com.aidevops.gateway.service;

import com.aidevops.gateway.dto.ChatRequest;
import com.aidevops.gateway.dto.ChatResponse;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class ChatService {

    public ChatResponse createPlaceholderResponse(ChatRequest request) {
        long startedAt = System.nanoTime();
        String userId = request.userId() == null || request.userId().isBlank()
                ? "anonymous"
                : request.userId();

        long latencyMs = (System.nanoTime() - startedAt) / 1_000_000;

        return new ChatResponse(
                UUID.randomUUID().toString(),
                userId,
                "Gateway placeholder response. AI Service integration will be added in a later phase.",
                "gateway-placeholder",
                latencyMs
        );
    }
}
