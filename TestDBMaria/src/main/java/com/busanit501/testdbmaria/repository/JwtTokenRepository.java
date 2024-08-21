package com.busanit501.testdbmaria.repository;

import com.busanit501.testdbmaria.entity.JwtToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JwtTokenRepository extends JpaRepository<JwtToken, Long> {
    // AccessToken으로 JwtToken을 조회하는 메서드
    Optional<JwtToken> findByToken(String token);

    List<JwtToken> findByName(String name);


    // AccessToken으로 JwtToken을 삭제하는 메서드
    void deleteByToken(String token);
}
