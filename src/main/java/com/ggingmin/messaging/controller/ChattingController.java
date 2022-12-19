package com.ggingmin.messaging.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ggingmin.messaging.config.RedisMessageListener;
import com.ggingmin.messaging.model.ChattingMessage;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@AllArgsConstructor
@RestController
public class ChattingController implements RedisMessageListener {

    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public String topic() {
        return "chat";
    }

    @PostMapping("/api/chat")
    public void publishMessage(@RequestBody ChattingMessage chattingMessage) throws JsonProcessingException {
        redisTemplate.convertAndSend(topic(), objectMapper.writeValueAsString(chattingMessage));
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String topic = new String(message.getChannel());
        try {
            ChattingMessage chattingMessage = objectMapper.readValue(message.getBody(), ChattingMessage.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
