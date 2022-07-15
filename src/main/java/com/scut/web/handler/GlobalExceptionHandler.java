package com.scut.web.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scut.constant.HttpStatus;
import com.scut.core.AjaxResult;
import com.scut.exception.TokenNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;


@RestControllerAdvice
public class GlobalExceptionHandler {
    @Autowired
    private ObjectMapper objectMapper;

    @ExceptionHandler(AccessDeniedException.class)
    public Mono<String> handleAccessDeniedException(AccessDeniedException e, ServerHttpRequest request) throws JsonProcessingException {
        return Mono.just(objectMapper.writeValueAsString(
                AjaxResult.error(HttpStatus.FORBIDDEN, "您没有权限访问")
        ));
    }

    @ExceptionHandler(TokenNotFoundException.class)
    public Mono<String> handleTokenNotFoundException(TokenNotFoundException e, ServerHttpRequest request) throws JsonProcessingException {
        return Mono.just(objectMapper.writeValueAsString(
                AjaxResult.error(HttpStatus.ERROR, "Token 无效")
        ));
    }


}
