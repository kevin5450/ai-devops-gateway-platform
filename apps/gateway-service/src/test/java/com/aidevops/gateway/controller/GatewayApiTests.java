package com.aidevops.gateway.controller;

import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.blankOrNullString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class GatewayApiTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void healthReturnsGatewayStatus() throws Exception {
        mockMvc.perform(get("/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"))
                .andExpect(jsonPath("$.service").value("gateway-service"))
                .andExpect(jsonPath("$.timestamp", not(blankOrNullString())));
    }

    @Test
    void chatReturnsPlaceholderResponse() throws Exception {
        mockMvc.perform(post("/api/chat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "prompt": "hello",
                                  "userId": "demo-user"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.requestId", not(blankOrNullString())))
                .andExpect(jsonPath("$.userId").value("demo-user"))
                .andExpect(jsonPath("$.provider").value("gateway-placeholder"))
                .andExpect(jsonPath("$.message", not(blankOrNullString())))
                .andExpect(jsonPath("$.latencyMs").isNumber());
    }

    @Test
    void chatRejectsBlankPrompt() throws Exception {
        mockMvc.perform(post("/api/chat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "prompt": "",
                                  "userId": "demo-user"
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.path").value("/api/chat"))
                .andExpect(jsonPath("$.message", not(blankOrNullString())));
    }
}
