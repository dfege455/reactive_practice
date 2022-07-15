package com.scut.service;

import com.scut.core.domain.model.LoginUser;
import com.scut.core.redis.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class SysLoginService {
    @Autowired
    private TokenService tokenService;

    @Autowired
    private ReactiveAuthenticationManager authenticationManager;

    @Autowired
    private RedisCache redisCache;

    public Mono<String> login(String username, String password){
//        Authentication authentication;
//        authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password)).block();
//        if(authentication == null){
//            throw new RuntimeException("用户不存在或用户名与密码不匹配！");
//        }
//        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
//        return tokenService.createToken(loginUser);

        return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password))
                .switchIfEmpty(Mono.error(new RuntimeException("用户不存在或用户名与密码不匹配！")))
                .flatMap(authentication -> {
            LoginUser loginUser = (LoginUser) authentication.getPrincipal();
            return tokenService.createToken(loginUser);
        });
    }

}
