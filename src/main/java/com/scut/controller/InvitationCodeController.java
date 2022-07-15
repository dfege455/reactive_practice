package com.scut.controller;

import com.scut.core.AjaxResult;
import com.scut.core.domain.model.InvitationBody;
import com.scut.core.redis.RedisCache;
import com.scut.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
public class InvitationCodeController {
    @Autowired
    private RedisCache redisCache;

    private static final String CODE_PREFIX = "invitation_code:";

    @PostMapping("/invitation")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Mono<AjaxResult> setInvitationCode(@RequestBody InvitationBody invitationBody) {
        String ciphertext = SecurityUtils.encryptPassword(invitationBody.getCode());
        return redisCache.setCacheObject(CODE_PREFIX + UUID.randomUUID(),
                invitationBody.getRole() + ":" + ciphertext).map(b -> AjaxResult.success());
    }
}
