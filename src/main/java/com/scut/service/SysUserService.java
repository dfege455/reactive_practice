package com.scut.service;

import com.scut.domain.SysUser;
import com.scut.repository.SysUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class SysUserService {

    @Autowired
    private SysUserRepository sysUserRepository;

    public Mono<Boolean> registerUser(SysUser user) {
        return sysUserRepository.save(user).map(res -> res.getUserId() > 0);
    }
}
