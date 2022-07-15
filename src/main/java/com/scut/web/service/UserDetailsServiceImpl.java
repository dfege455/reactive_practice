package com.scut.web.service;

import com.scut.core.domain.model.LoginUser;
import com.scut.repository.SysUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class UserDetailsServiceImpl implements ReactiveUserDetailsService {

    @Autowired
    SysUserRepository sysUserRepository;

//    @Autowired
//    Scheduler scheduler;

    @Override
    public Mono<UserDetails> findByUsername(String username) throws UsernameNotFoundException {
        return sysUserRepository.findByName(username).map(LoginUser::new);}
}
