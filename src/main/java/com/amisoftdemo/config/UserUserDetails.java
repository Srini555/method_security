package com.amisoftdemo.config;

import com.amisoftdemo.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class UserUserDetails implements UserDetails {

    private final User usr;

    private final Set<GrantedAuthority> authorities;

    public UserUserDetails(User user) {

        this.usr = user;
        this.authorities = this.usr.getAuthorities()
                .stream()
                .map(au -> new SimpleGrantedAuthority("ROLE_"+au.getAuthority()))
                .collect(Collectors.toSet());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.usr.getPassword();
    }

    @Override
    public String getUsername() {
        return this.usr.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public User getUsr() {
        return usr;
    }
}
