package com.scut.core.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.scut.domain.SysUser;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class LoginUser implements UserDetails, Serializable {

    private static final long serialVersionUID = 442353225232L;
    @Setter @Getter private SysUser sysUser;
    @Setter @Getter private String token;
    @Setter @Getter private Long expireTime;
    @Setter @Getter private Long loginTime;
    //@Setter @Getter private Set<String> permissions;

    public LoginUser(SysUser sysUser){
        this.sysUser = sysUser;
    }

    @JsonIgnore @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        //return permissions.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
        ArrayList<SimpleGrantedAuthority> permission = new ArrayList<>();
        permission.add(new SimpleGrantedAuthority(sysUser.getRole()));
        return permission;
    }

    @JsonIgnore @Override
    public String getPassword() {
        return sysUser.getPassword();
    }

    @JsonIgnore @Override
    public String getUsername() {
        return sysUser.getName();
    }

    @JsonIgnore @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonIgnore @Override
    public boolean isEnabled() {
        return true;
    }
}
