package com.scut.service;

import com.scut.core.redis.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysConfigService {

    @Autowired
    private RedisCache redisCache;


}
