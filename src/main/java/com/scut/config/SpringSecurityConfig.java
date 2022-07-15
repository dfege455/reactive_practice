package com.scut.config;

import com.scut.repository.SysUserRepository;
import com.scut.web.security.JwtAuthenticationTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.security.web.server.authentication.logout.ServerLogoutSuccessHandler;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;

@Configuration
@EnableReactiveMethodSecurity
@EnableWebFluxSecurity
public class SpringSecurityConfig{

    @Autowired
    private JwtAuthenticationTokenFilter authenticationTokenFilter;

    @Autowired
    private ServerLogoutSuccessHandler logoutSuccessHandler;

    @Autowired
    private ServerAuthenticationEntryPoint unauthorizedHandler;

    @Bean
    SecurityWebFilterChain filterChain(ServerHttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler)
                .and()
                .httpBasic().disable()
                .headers().frameOptions().disable()
                .and()
                //.x509(withDefaults())
                //.httpBasic(withDefaults())
                .authorizeExchange((exchanges) -> exchanges
                        .pathMatchers("/login", "/logout", "/register").permitAll()
//                        .pathMatchers(HttpMethod.GET, "/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**").permitAll()
//                        .pathMatchers("/swagger-ui/index.html").permitAll()
                        .anyExchange().authenticated()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessHandler(logoutSuccessHandler)
                        .requiresLogout(ServerWebExchangeMatchers.pathMatchers("/logout")
                        )
                );
        http.addFilterAt(authenticationTokenFilter, SecurityWebFiltersOrder.HTTP_BASIC);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
