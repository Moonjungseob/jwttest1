package com.busanit501.testdbmaria.service;

import com.busanit501.testdbmaria.entity.JwtToken;
import com.busanit501.testdbmaria.repository.JwtTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class JwtTokenService {
    @Autowired
    private JwtTokenRepository jwtTokenRepository;

    public void saveToken(String token, String refreshToken, String name, LocalDateTime issuedAt, LocalDateTime expiresAt, LocalDateTime refreshExpiresAt) {
        // 기존 토큰이 있는지 확인
        JwtToken existingToken = findByName(name);

        if (existingToken != null) {
            // 기존 토큰이 있으면 업데이트
            existingToken.setToken(token);
            existingToken.setRefreshToken(refreshToken);
            existingToken.setIssuedAt(issuedAt);
            existingToken.setExpiresAt(expiresAt);
            existingToken.setRefreshExpiresAt(refreshExpiresAt);

            // 업데이트된 토큰을 저장
            jwtTokenRepository.save(existingToken);
        } else {
            // 기존 토큰이 없으면 새 토큰 저장
            JwtToken jwtToken = new JwtToken();
            jwtToken.setToken(token);
            jwtToken.setRefreshToken(refreshToken);
            jwtToken.setName(name);
            jwtToken.setIssuedAt(issuedAt);
            jwtToken.setExpiresAt(expiresAt);
            jwtToken.setRefreshExpiresAt(refreshExpiresAt);

            jwtTokenRepository.save(jwtToken);
        }
    }
    // 토큰 조회
    public Optional<JwtToken> findByToken(String token) {
        return jwtTokenRepository.findByToken(token);
    }

    public JwtToken findByName(String name) {
        List<JwtToken> tokens = jwtTokenRepository.findByName(name);
        if (tokens.isEmpty()) {
            return null; // 또는 적절한 처리
        } else if (tokens.size() > 1) {
            // 적절한 처리 예: throw new IllegalStateException("Multiple tokens found");
        }
        return tokens.get(0);
    }
    public JwtToken save(JwtToken jwtToken) {
        return jwtTokenRepository.save(jwtToken);
    }

    // 토큰 삭제 (로그아웃 또는 토큰 무효화 시 사용)
    public void deleteToken(String token) {
        jwtTokenRepository.deleteByToken(token);
    }
}
