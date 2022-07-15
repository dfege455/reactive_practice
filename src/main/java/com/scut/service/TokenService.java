package com.scut.service;

import com.scut.constant.Constants;
import com.scut.core.domain.model.LoginUser;
import com.scut.core.redis.RedisCache;
import com.scut.exception.TokenNotFoundException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.*;

@Service
@Slf4j
public class TokenService {

    protected static final long MILLIS_SECOND = 1000;
    protected static final long MILLIS_MINUTE = 60 * MILLIS_SECOND;
    private static final Long MILLIS_MINUTE_TEN = 20 * 60 * 1000L;

    @Value("${token.header}")
    private String header;

    @Value("${token.secret}")
    private String secret;

    @Value("${token.expireTime}")
    private int expireTime;

    @Autowired
    private RedisCache redisCache;

    private String getToken(ServerHttpRequest request){
        String token = request.getHeaders().getFirst(header);
        if(token != null && token.startsWith(Constants.TOKEN_PREFIX)){
            token = token.replace(Constants.TOKEN_PREFIX, "");
        }
        return token;
    }

    public String getUsernameFromToken(String token) {
        return parseToken(token).getSubject();
    }

    private String getTokenKey(String uuid){
        return Constants.LOGIN_USER_KEY + uuid;
    }

    private Claims parseToken(String token){
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

    private String createToken(Map<String, Object> claims){
        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, secret).compact();
    }

    public Mono<String> createToken(LoginUser loginUser){
        String token = UUID.randomUUID().toString();
        loginUser.setToken(token);
        return refreshToken(loginUser).map(b -> {
            Map<String, Object> claims = new HashMap<>();
            claims.put(Constants.LOGIN_USER_KEY, token);
            return createToken(claims);
        });
    }

    public Mono<Boolean> verifyToken(LoginUser loginUser) {
        long expireTime = loginUser.getExpireTime();
        long currentTime = System.currentTimeMillis();
        if(expireTime - currentTime <= MILLIS_MINUTE_TEN) {
            return refreshToken(loginUser);
        }
        return Mono.just(false);
    }

    public Mono<Boolean> refreshToken(LoginUser loginUser){
        loginUser.setLoginTime(System.currentTimeMillis());
        loginUser.setExpireTime(loginUser.getLoginTime() + expireTime * MILLIS_MINUTE);
        String userKey = getTokenKey(loginUser.getToken());
        return redisCache.setCacheObject(userKey, loginUser, Duration.ofMinutes(expireTime));
    }

    public Mono<LoginUser> getLoginUser(ServerHttpRequest request){
        String token = getToken(request);
        if(token != null && !"".equals(token)){
            Claims claims = parseToken(token);
            String uuid = (String) claims.get(Constants.LOGIN_USER_KEY);
            String userKey = getTokenKey(uuid);
            return redisCache.getCacheObject(userKey).cast(LoginUser.class).switchIfEmpty(Mono.error(new TokenNotFoundException()));
        }
        return Mono.just(new LoginUser(null));
    }

    public Mono<Boolean> setLoginUser(LoginUser loginUser){
        if(loginUser != null && !"".equals(loginUser.getToken())){
            return refreshToken(loginUser);
        }
        return Mono.just(false);
    }

    public Mono<Long> delLoginUser(String token){
        if(!"".equals(token)){
            String userKey = getTokenKey(token);
            return redisCache.deleteObject(userKey).switchIfEmpty(Mono.error(new TokenNotFoundException()));
        }
        return Mono.just(-1L);
    }


}
