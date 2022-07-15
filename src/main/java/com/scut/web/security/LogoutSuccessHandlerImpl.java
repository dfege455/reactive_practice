package com.scut.web.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scut.constant.HttpStatus;
import com.scut.core.AjaxResult;
import com.scut.core.domain.model.LoginUser;
import com.scut.service.TokenService;
import com.scut.utils.ServerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.logout.ServerLogoutSuccessHandler;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class LogoutSuccessHandlerImpl implements ServerLogoutSuccessHandler {
    @Autowired
    private TokenService tokenService;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public Mono<Void> onLogoutSuccess(WebFilterExchange exchange, Authentication authentication) {
//        LoginUser loginUser = tokenService.getLoginUser(exchange.getExchange().getRequest());
//        if(loginUser != null){
//            String username = loginUser.getUsername();
//            tokenService.delLoginUser(loginUser.getToken());
//            return Mono.empty();
//        }
//        return Mono.empty();
        return tokenService.getLoginUser(exchange.getExchange().getRequest()).flatMap(loginUser -> {
//            if(loginUser.getToken() != null){
//                //String username = loginUser.getUsername();
//                return tokenService.delLoginUser(loginUser.getToken()).mapNotNull(l -> null);
//            }
//            try {
//                return ServerUtils.renderString(exchange.getExchange().getResponse(),
//                        objectMapper.writeValueAsString(AjaxResult.error(HttpStatus.SUCCESS, "退出成功")));
//            } catch (Exception e) {
//                return Mono.error(new RuntimeException(e));
//            }
            if(loginUser.getToken() != null) {
                return tokenService.delLoginUser(loginUser.getToken()).flatMap(l -> {
                    try {
                        return ServerUtils.renderString(exchange.getExchange().getResponse(),
                                objectMapper.writeValueAsString(AjaxResult.error(HttpStatus.SUCCESS, "退出成功")));
                    } catch (Exception e) {
                        return Mono.error(new RuntimeException(e));
                    }
                });
            }
            try {
                return ServerUtils.renderString(exchange.getExchange().getResponse(),
                        objectMapper.writeValueAsString(AjaxResult.error("注销失败")));
            } catch (Exception e) {
                return Mono.error(new RuntimeException(e));
            }
        });
    }
}
