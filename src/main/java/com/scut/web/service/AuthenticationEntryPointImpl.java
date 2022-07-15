package com.scut.web.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scut.constant.HttpStatus;
import com.scut.core.AjaxResult;
import com.scut.utils.ServerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.io.Serializable;
import java.util.HashMap;

@Component
public class AuthenticationEntryPointImpl implements ServerAuthenticationEntryPoint, Serializable {
    private static final long serialVersionUID = -982789097774862L;
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException ex) {
        int code = HttpStatus.UNAUTHORIZED;
        String msg = String.format("请求访问：%s，认证失败，无法访问系统资源", exchange.getRequest().getPath());
        try {
            return ServerUtils.renderString(exchange.getResponse(),
                    objectMapper.writeValueAsString(AjaxResult.error(code, msg)));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
