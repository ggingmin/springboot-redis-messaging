package com.ggingmin.messaging.controller;

import com.ggingmin.messaging.model.ChattingMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.web.bind.annotation.*;

@Slf4j
@AllArgsConstructor
@RestController
public class RedisPubSubController implements MessageListener {

    private final RedisTemplate<String, ChattingMessage> redisTemplate;
    private final RedisMessageListenerContainer redisMessageListenerContainer;
//    private final ObjectMapper objectMapper;



    @PostMapping("/rooms/{roomId}")
    public void createRoom(@PathVariable String roomId) {
        redisMessageListenerContainer.addMessageListener(this, new ChannelTopic(roomId));
    }

    @DeleteMapping("/rooms/{roomId}")
    public void deleteRoom(@PathVariable String roomId) {
        redisMessageListenerContainer.removeMessageListener(this, new ChannelTopic(roomId));
    }


    @PostMapping("/rooms/{roomId}/chat")
    public void sendMessage(@PathVariable String roomId, @RequestBody ChattingMessage message) {
        redisTemplate.convertAndSend(roomId, message);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String roomId = new String(message.getChannel());
        ChattingMessage chattingMessage = (ChattingMessage) redisTemplate.getValueSerializer().deserialize(message.getBody());

        log.info("[메세지 이벤트] 방번호: " + roomId + " 메세지: " + chattingMessage);

    }
}
