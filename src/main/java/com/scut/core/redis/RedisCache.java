package com.scut.core.redis;

import com.scut.core.domain.model.RedisObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.ReactiveValueOperations;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class RedisCache {
    @Autowired
    public ReactiveRedisTemplate<String, Object> redisTemplate;

    public <T> Mono<Boolean> setCacheObject(final String key, final T value){
        return redisTemplate.opsForValue().set(key, value);
    }

    public <T> Mono<Boolean> setCacheObject(final String key, final T value,
                                            final Duration duration){
        return redisTemplate.opsForValue().set(key, value, duration);
    }


    public Mono<Object> getCacheObject(final String key){
        ReactiveValueOperations<String, Object> operations = redisTemplate.opsForValue();
        return operations.get(key);
    }

    public Mono<Long> deleteObject(final String key){
        return redisTemplate.delete(key);
    }

    public Flux<String> getKeys(String pattern) {
        return redisTemplate.keys(pattern);
    }

    public Flux<RedisObject> getKeysAndValues(String pattern) {
//        return getKeys(pattern).flatMap(this::getCacheObject).cast(String.class);
//        getKeys(pattern).map(key -> new RedisObject(key, (String) getCacheObject(key).block()));
        return getKeys(pattern).flatMap(key -> getCacheObject(key).cast(String.class).map(value -> new RedisObject(key, value)));
    }
}
