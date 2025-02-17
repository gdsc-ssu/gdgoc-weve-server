package com.weve.security;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Getter
public class CustomUserDetails implements UserDetails {
    private final String phoneNumber;

    public CustomUserDetails(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null; // 권한 정보가 필요하면 여기에 추가
    }

    @Override
    public String getPassword() {
        return null; // 비밀번호가 필요하지 않음 (JWT 인증)
    }

    @Override
    public String getUsername() {
        return this.phoneNumber; // 사용자 식별자로 전화번호 사용
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
}
