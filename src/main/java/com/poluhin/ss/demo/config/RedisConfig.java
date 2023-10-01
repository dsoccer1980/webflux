package com.poluhin.ss.demo.config;

import com.poluhin.ss.demo.domain.model.AuthResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

@Component
public class RedisConfig {

  @Bean
  ReactiveRedisOperations<String, AuthResponse> redisOperations(ReactiveRedisConnectionFactory factory) {
    Jackson2JsonRedisSerializer<AuthResponse> serializer = new Jackson2JsonRedisSerializer<>(AuthResponse.class);

    RedisSerializationContext.RedisSerializationContextBuilder<String, AuthResponse> builder =
        RedisSerializationContext.newSerializationContext(new StringRedisSerializer());

    RedisSerializationContext<String, AuthResponse> context = builder.value(serializer).build();

    return new ReactiveRedisTemplate<>(factory, context);
  }

}
