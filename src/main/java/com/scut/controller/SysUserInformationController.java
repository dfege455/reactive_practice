package com.scut.controller;

import com.scut.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class SysUserInformationController {
    @Autowired
    private TokenService tokenService;

    @GetMapping("/role")
    public Mono<String> getUserRole(ServerHttpRequest httpRequest) {
        return tokenService.getLoginUser(httpRequest).map(lu -> lu.getSysUser().getRole());
    }
}
