package com.scut.repository;


import com.scut.domain.SysUser;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface SysUserRepository extends ReactiveCrudRepository<SysUser, Integer> {
    Mono<SysUser> findByName(String name);
}
