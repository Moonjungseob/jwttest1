package com.busanit501.testdbmaria.secureity;

import com.busanit501.testdbmaria.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class UserDetailsImpl implements UserDetails {

    private final User user;

    public UserDetailsImpl(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 권한 설정, 이 예제에서는 단일 역할을 부여합니다.
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        // User 엔티티에 비밀번호 필드가 있다면 해당 값을 반환합니다.
        // 현재 엔티티에는 없으므로 null 반환 (추가 필요)
        return null;
    }

    @Override
    public String getUsername() {
        // 인증에 사용할 사용자 이름 반환 (이 경우 이메일을 사용 가능)
        return user.getEmail();
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

    public User getUser() {
        return user;
    }
}