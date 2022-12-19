package com.ggingmin.messaging.config;

import org.springframework.data.redis.connection.MessageListener;

public interface RedisMessageListener extends MessageListener {

    String topic();
}
