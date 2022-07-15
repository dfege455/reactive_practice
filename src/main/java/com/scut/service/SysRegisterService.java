package com.scut.service;

import com.scut.core.domain.model.LoginBody;
import com.scut.core.redis.RedisCache;
import com.scut.domain.SysUser;
import com.scut.utils.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;


@Service
@Slf4j
public class SysRegisterService {
    private static final String CODE_PREFIX = "invitation_code:*";
    @Autowired
    private RedisCache redisCache;

    @Autowired
    private SysUserService sysUserService;

    public Mono<String> checkCode(String code) {
        return redisCache.getKeysAndValues(CODE_PREFIX).collect(Collectors.toList()).flatMap(redisObjects -> {
            int findIndex = -1;
            String findRole = "";
            for(int i = 0; i < redisObjects.size(); i++) {
                String value = redisObjects.get(i).getValue();
                int index = value.indexOf(':');
                String role = value.substring(0, index);
                String ciphertext = value.substring(index + 1);
                if(SecurityUtils.matchesPassword(code, ciphertext)) {
                    findIndex = i;
                    findRole = role;
                    break;
                }
            }
            if(findIndex == -1) return Mono.just("");
            String finalFindRole = findRole;
            return redisCache.deleteObject(redisObjects.get(findIndex).getKey()).map(l -> finalFindRole);
        }).switchIfEmpty(Mono.just(""));
    }
    //        var redisObjects = redisCache.getKeysAndValues(CODE_PREFIX).collect(Collectors.toList()).block();
//        if(redisObjects == null) return Mono.just("");
//        int findIndex = -1;
//        String findRole = "";
//        for(int i = 0; i < redisObjects.size(); i++) {
//            String value = redisObjects.get(i).getValue();
//            int index = value.indexOf(':');
//            String role = value.substring(0, index);
//            String ciphertext = value.substring(index + 1);
//            if(SecurityUtils.matchesPassword(code, ciphertext)) {
//                findIndex = i;
//                findRole = role;
//                break;
//            }
//        }
//        if(findIndex == -1) return Mono.just("");
//        redisCache.deleteObject(redisObjects.get(findIndex).getKey()).block();
//        return Mono.just(findRole);


    public Mono<String> register(LoginBody registerBody, String role){

        String username = registerBody.getName();
        String password = registerBody.getPassword();

        SysUser sysUser = SysUser.builder()
                .name(username)
                .password("{bcrypt}" + (new BCryptPasswordEncoder().encode(password)))
                .role(role)
                .build();
        return sysUserService.registerUser(sysUser).map(b -> {
            String msg = "";
            if(!b){
                msg = "注册失败";
            }
            return msg;
        });
    }

}
