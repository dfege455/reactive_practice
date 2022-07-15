package com.scut.utils;


import com.scut.constant.HttpStatus;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class ServerUtils {
    public static Mono<Void> renderString(ServerHttpResponse response, String str){
        response.setRawStatusCode(HttpStatus.SUCCESS);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        return response.writeWith(Mono.just(
                new DefaultDataBufferFactory().wrap(
                        ByteBuffer.wrap(str.getBytes(StandardCharsets.UTF_8))
                )
        ));
    }

}
