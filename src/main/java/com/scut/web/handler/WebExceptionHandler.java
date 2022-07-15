package com.scut.web.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scut.core.AjaxResult;
import com.scut.exception.TokenNotFoundException;
import com.scut.utils.ServerUtils;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class WebExceptionHandler implements ErrorWebExceptionHandler {
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        exchange.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);

        if(ex instanceof MalformedJwtException || ex instanceof SignatureException || ex instanceof TokenNotFoundException) {
            try {
                return ServerUtils.renderString(exchange.getResponse(), objectMapper.writeValueAsString(AjaxResult.error(com.scut.constant.HttpStatus.FORBIDDEN, ex.getMessage())));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }else {
            try {
                return ServerUtils.renderString(exchange.getResponse(), objectMapper.writeValueAsString(AjaxResult.error(com.scut.constant.HttpStatus.ERROR, ex.getMessage())));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }

    }
}
