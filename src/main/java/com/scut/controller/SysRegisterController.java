package com.scut.controller;

import com.scut.core.AjaxResult;
import com.scut.core.domain.model.LoginBody;
import com.scut.core.domain.model.RegisterBody;
import com.scut.service.SysRegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class SysRegisterController {
    @Autowired
    SysRegisterService sysRegisterService;

    @PostMapping("/register")
    public Mono<AjaxResult> register(@RequestBody RegisterBody registerBody) {

//        return sysRegisterService.register(user)
//                .map(msg -> "".equals(msg) ? AjaxResult.success() : AjaxResult.error(msg));

//        String role = sysRegisterService.checkCode(registerBody.getInvitationCode()).block();
//        if(!"".equals(role)){
//            String msg = sysRegisterService.register(new LoginBody(registerBody.getName(), registerBody.getPassword()), role).block();
//            return "".equals(msg) ? Mono.just(AjaxResult.success()) : Mono.just(AjaxResult.error(msg));
//        }else {
//            return Mono.just(AjaxResult.error("注册失败！"));
//        }

        return sysRegisterService.checkCode(registerBody.getInvitationCode()).flatMap(role -> {
            if(!"".equals(role)) {
                return sysRegisterService.register(new LoginBody(registerBody.getName(), registerBody.getPassword()), role).map(msg ->
                        "".equals(msg) ? AjaxResult.success() : AjaxResult.error(msg));
            }
            return Mono.just(AjaxResult.error("注册失败！"));
        });
    }
}
