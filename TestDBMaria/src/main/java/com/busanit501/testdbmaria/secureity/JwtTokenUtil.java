package com.busanit501.testdbmaria.secureity;

import com.busanit501.testdbmaria.entity.JwtToken;
import com.busanit501.testdbmaria.service.JwtTokenService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@Log4j2
@Component
public class JwtTokenUtil {

    private String SECRET_KEY = "your_super_secret_key_which_is_long_enough_for_hmac_sha256";

    // 토큰 유효 시간: 10시간
    public final long JWT_EXPIRATION = 1000 * 60* 60;
    public static final long REFRESH_TOKEN_EXPIRATION_TIME = 1000 * 60 * 60 * 24 * 7; // 7일

    @Autowired
    private JwtTokenService jwtTokenService;

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes()); // SECRET_KEY로부터 서명 키 생성
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException e) {
            log.error("JWT 파싱 중 오류 발생: ", e);
            throw new RuntimeException("Invalid JWT token", e);
        }
    }

    public Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // 토큰 생성
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername());
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // 토큰 검증
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        log.info("유저이름 가져오니? "+username);
        Optional<JwtToken> storedTokenOpt = jwtTokenService.findByToken(token);
        log.info("토큰이름 가져오니? "+storedTokenOpt);

        if (storedTokenOpt.isEmpty()) {
            return false;
        }

        JwtToken storedToken = storedTokenOpt.get();
        boolean isValid = (username.equals(userDetails.getUsername()) && !isTokenExpired(token) && !isTokenExpiredInDB(storedToken));

        if (!isValid) {
            log.warn("토큰 검증 실패: 유저 이름 일치 여부: " + username.equals(userDetails.getUsername())
                    + ", 토큰 만료 여부: " + isTokenExpired(token)
                    + ", DB 토큰 만료 여부: " + isTokenExpiredInDB(storedToken));
        }

        return isValid;
    }

    private boolean isTokenExpiredInDB(JwtToken storedToken) {
        return storedToken.getExpiresAt().isBefore(LocalDateTime.now());
    }

    public String generateRefreshToken(String name) {
        return Jwts.builder()
                .setSubject(name)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION_TIME))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }
}
