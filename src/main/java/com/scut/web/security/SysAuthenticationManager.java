package com.scut.web.security;

import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class SysAuthenticationManager extends UserDetailsRepositoryReactiveAuthenticationManager {

    public SysAuthenticationManager(ReactiveUserDetailsService userDetailsService) {
        super(userDetailsService);
    }

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        if(authentication.isAuthenticated()){
            return Mono.just(authentication);
        }

        return super.authenticate(authentication);
    }
}
