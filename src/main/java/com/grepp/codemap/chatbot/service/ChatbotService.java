package com.grepp.codemap.chatbot.service;

import com.grepp.codemap.chatbot.llm.Assistant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatbotService {

    private final Assistant assistant;

    public String getRecommendation(String keyword) {
        return assistant.recommendPlan(keyword);
    }
}
