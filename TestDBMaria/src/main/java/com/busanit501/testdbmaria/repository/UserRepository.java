package com.busanit501.testdbmaria.repository;

import com.busanit501.testdbmaria.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByName(String name);  // 이름로 사용자 조회
}