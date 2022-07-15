package com.scut.utils;

import com.scut.core.domain.model.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import reactor.core.publisher.Mono;

public class SecurityUtils {
    private static final PasswordEncoder passwordEncoder;

    static {
        passwordEncoder = new BCryptPasswordEncoder();
    }

    public static Mono<Authentication> getAuthentication() {
        return ReactiveSecurityContextHolder.getContext().map(SecurityContext::getAuthentication);
    }

    public static Mono<LoginUser> getLoginUser() {
        return getAuthentication().map(au -> (LoginUser) au.getPrincipal());
    }

//    public static Long getUserId() {
//        return getLoginUser().map(lu -> lu.g());
//    }

    public static Mono<String> getUsername() {
        return getLoginUser().map(LoginUser::getUsername);
    }

    public static boolean matchesPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    public static String encryptPassword(String password) {
        return passwordEncoder.encode(password);
    }
}
