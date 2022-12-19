package com.ggingmin.messaging.config;

import com.ggingmin.messaging.model.ChattingMessage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.List;

@Configuration
public class RedisConfig {

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(
            RedisConnectionFactory redisConnectionFactory,
            List<RedisMessageListener> redisMessageListeners
    ) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory);
        redisMessageListeners.forEach(listener -> container.addMessageListener(listener, new ChannelTopic(listener.topic())));
        return container;
    }

    @Bean
    public RedisTemplate<String, ChattingMessage> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, ChattingMessage> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(ChattingMessage.class));
        return redisTemplate;
    }
}
