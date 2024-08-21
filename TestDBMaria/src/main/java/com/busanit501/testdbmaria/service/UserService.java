package com.busanit501.testdbmaria.service;

import com.busanit501.testdbmaria.entity.User;
import com.busanit501.testdbmaria.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.context.annotation.Lazy;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    @Lazy
    private PasswordEncoder passwordEncoder;


    public User saveUser(User user) {
        // 비밀번호를 암호화한 후 저장
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // 역할을 숫자로 설정 (기본값: 0 -> 일반 사용자)
        user.setRole(user.getRole() == 1 ? 1 : 0);

        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
