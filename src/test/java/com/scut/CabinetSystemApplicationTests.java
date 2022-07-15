package com.scut;

import com.scut.controller.HelloController;
import com.scut.controller.InvitationCodeController;
import com.scut.core.domain.model.InvitationBody;
import com.scut.core.domain.model.LoginUser;
import com.scut.core.redis.RedisCache;
import com.scut.domain.SysUser;
import com.scut.repository.SysUserRepository;
import com.scut.service.SysRegisterService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.Assert;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Optional;

@SpringBootTest
class CabinetSystemApplicationTests {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    SysUserRepository sysUserRepository;

    @Autowired
    HelloController helloController;

    @Autowired
    RedisCache redisCache;

    @Test
    void generatePassword() {
        System.out.println(passwordEncoder.encode("password1"));
        System.out.println(passwordEncoder.encode("password2"));
        System.out.println(passwordEncoder.encode("hello"));

    }

    @Test
    void checkPassword() {
        Assert.isTrue(passwordEncoder.matches("password1", "$2a$10$QkWxwKfSH9uS/gvMmJdiPeTjItc6vz1j2TnBdhHPQeotrR41em7pS"), "they are not equal");
    }

    @Test
    void checkDB(){
        System.out.println("Hello");
        for(var x : sysUserRepository.findAll().toIterable()){
            System.out.println(x);
        }
    }

    @Test
    void checkReactor(){
        StepVerifier.create(Mono.just(0).mapNotNull(i -> null))
//                .expectNext("one")
//                .expectNext("two")
                .expectComplete()
                .verify();
    }

    @Test
    void checkSerialize(){
//        redisCache.setCacheObject("my_object", new LoginUser(SysUser.builder().userId(10).name("ouguang").password("{bcrypt}$sjojgon").build())).block();
        var loginUser = redisCache.getCacheObject("my_object").block();
        System.out.println(loginUser);
    }

    @Test
    void checkNull(){
        StepVerifier.create(ReactiveSecurityContextHolder.getContext())
                .expectComplete()
                .verify();
    }

    @Autowired
    InvitationCodeController invitationCodeController;

    @Test
    void checkInvitation() {
        invitationCodeController.setInvitationCode(new InvitationBody("ouye", "ROLE_ADMIN")).block();
        invitationCodeController.setInvitationCode(new InvitationBody("guangye", "ROLE_ADMIN")).block();
        invitationCodeController.setInvitationCode(new InvitationBody("ziyi", "ROLE_BOSS")).block();
    }

    @Autowired
    SysRegisterService sysRegisterService;

    @Test
    void checkInvitation2() {
        System.out.println(sysRegisterService.checkCode("ouye").block());
        System.out.println(sysRegisterService.checkCode("ouguangye").block());
        System.out.println(sysRegisterService.checkCode("guangye").block());
        System.out.println(sysRegisterService.checkCode("ziyi").block());
        System.out.println(sysRegisterService.checkCode("yangziyi").block());
    }
}
