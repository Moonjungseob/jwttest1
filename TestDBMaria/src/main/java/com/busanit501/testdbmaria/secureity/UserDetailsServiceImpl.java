package com.busanit501.testdbmaria.secureity;

import com.busanit501.testdbmaria.entity.User;
import com.busanit501.testdbmaria.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 이메일로 사용자 조회
        Optional<User> optionalUser = userRepository.findByEmail(username);

        // 사용자 없으면 예외 발생
        User user = optionalUser
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));

        // UserDetails 객체 생성
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .authorities(Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")))
                .build();
    }
}