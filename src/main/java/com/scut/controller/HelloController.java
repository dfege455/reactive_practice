package com.scut.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scut.core.AjaxResult;
import com.scut.core.redis.RedisCache;
import com.scut.domain.SysUser;
import com.scut.repository.SysUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

@RestController
//@RequestMapping("/api")
@Slf4j
public class HelloController {

    @Autowired
    private SysUserRepository sysUserRepository;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ObjectMapper objectMapper;

    //@PreAuthorize("authenticated")
    @GetMapping("/hello")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Mono<String> getHello(){
        //return redisCache.getCacheObject("hello");
        return Mono.just("hello world");
    }

    //@PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/getAllHello")
    @PreAuthorize("hasRole('ROLE_BOSS') or hasRole('ROLE_ADMIN')")
    public Mono<AjaxResult> getAllHello(){
        return sysUserRepository.findAll().collect(Collectors.toList()).map(AjaxResult::success);
    }

    //@PreAuthorize("hasRole('USER')")
    @PostMapping("/insertHello")
    public Mono<Integer> insertHello(@RequestBody SysUser sysUser){
        Mono<SysUser> res = sysUserRepository.save(sysUser);
        return res.flatMap(t -> Mono.just(t.getUserId()));
    }
}
