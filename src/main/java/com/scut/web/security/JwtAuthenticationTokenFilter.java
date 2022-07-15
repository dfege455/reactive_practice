package com.scut.web.security;

import com.scut.core.domain.model.LoginUser;
import com.scut.service.TokenService;
import com.scut.utils.ServerUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Component
@Slf4j
public class JwtAuthenticationTokenFilter implements WebFilter {

    @Autowired
    private TokenService tokenService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        /* LoginUser loginUser = tokenService.getLoginUser(exchange.getRequest()).block();
        if(loginUser != null){
            tokenService.refreshToken(loginUser).block();
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    loginUser, null
            );
            return chain.filter(exchange).contextWrite(
                    ReactiveSecurityContextHolder.withAuthentication(authentication)
            );
        }
        return chain.filter(exchange);*/

        return tokenService.getLoginUser(exchange.getRequest()).flatMap(loginUser -> {
                if (loginUser.getToken() != null && !loginUser.getToken().equals("")) {
                    return tokenService.verifyToken(loginUser).flatMap(b -> {
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                loginUser, null, loginUser.getAuthorities()
                        );
                        return chain.filter(exchange).contextWrite(
                                ReactiveSecurityContextHolder.withAuthentication(authentication)
                        );
                    });
                }
            return chain.filter(exchange);
        });
    }
}
