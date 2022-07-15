package com.scut.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scut.core.AjaxResult;
import com.scut.core.domain.model.LoginBody;
import com.scut.service.SysLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@RestController
//@RequestMapping("/api")
public class SysLoginController {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private SysLoginService loginService;

    @PostMapping("/login")
    public Mono<AjaxResult> login(@RequestBody LoginBody loginBody){

//        String token = loginService.login(loginBody.getName(), loginBody.getPassword());
//        ajax.put("token", token);
//        return Mono.just(ajax);

        return loginService.login(loginBody.getName(), loginBody.getPassword()).map(token -> {
            AjaxResult ajax = AjaxResult.success();
            ajax.put("token", token);
            return ajax;
        });
    }
}
