package com.githubdoc.service;

import com.githubdoc.budget.BudgetTracker;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.http.MediaType;

import java.util.*;

@Service
public class PerplexityApiService {

    @Value("${perplexity.api.key}")
    private String apiKey;

    private final BudgetTracker budgetTracker;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public PerplexityApiService(BudgetTracker budgetTracker) {
        this.budgetTracker = budgetTracker;
    }

    private double estimateCost(int inputTokens, int outputTokens) {
        double inputCost = inputTokens / 1_000_000.0 * 3;   // $3 per million input tokens
        double outputCost = outputTokens / 1_000_000.0 * 15; // $15 per million output tokens
        return inputCost + outputCost;
    }

    public String getDocumentation(String prompt) {
        int inputTokens = prompt.length() / 4; // Rough estimate: 1 token ≈ 4 chars
        int outputTokens = 2000; // Estimate for a detailed README

        double estimatedCost = estimateCost(inputTokens, outputTokens);

        if (!budgetTracker.canSpend(estimatedCost)) {
            throw new RuntimeException("API budget exceeded. Remaining: $" + budgetTracker.getRemainingBudget());
        }

        WebClient client = WebClient.builder()
                .baseUrl("https://api.perplexity.ai")
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .build();

        // Build request body using Jackson for safety
        try {
            Map<String, Object> payload = new HashMap<>();
            payload.put("model", "sonar-pro");

            List<Map<String, String>> messages = new ArrayList<>();
            messages.add(Map.of("role", "system", "content", "You are a helpful documentation assistant."));
            messages.add(Map.of("role", "user", "content", prompt));
            payload.put("messages", messages);

            payload.put("max_tokens", outputTokens);

            String requestBody = objectMapper.writeValueAsString(payload);

            String jsonResponse = client.post()
                    .uri("/chat/completions")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            budgetTracker.spend(estimatedCost);

            // Parse JSON and extract only the assistant's message
            JsonNode root = objectMapper.readTree(jsonResponse);
            JsonNode choices = root.path("choices");
            if (choices.isArray() && choices.size() > 0) {
                JsonNode message = choices.get(0).path("message");
                if (message.has("content")) {
                    return message.get("content").asText();
                }
            }
            return "No documentation generated.";

        } catch (Exception e) {
            e.printStackTrace();
            return "Error generating documentation: " + e.getMessage();
        }
    }
}
