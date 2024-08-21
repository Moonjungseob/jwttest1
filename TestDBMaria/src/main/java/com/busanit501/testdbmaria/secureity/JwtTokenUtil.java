package com.busanit501.testdbmaria.secureity;

import com.busanit501.testdbmaria.entity.JwtToken;
import com.busanit501.testdbmaria.service.JwtTokenService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
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

@Component
public class JwtTokenUtil {

    private String SECRET_KEY = "your_secret_key";

    // 토큰 유효 시간: 10시간
    private final long JWT_EXPIRATION = 1000 * 60;
    private static final long REFRESH_TOKEN_EXPIRATION_TIME = 1000 * 60 * 60 * 24 * 7; // 7일

    @Autowired
    private JwtTokenService jwtTokenService;

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
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
            throw new RuntimeException("Invalid JWT token", e);
        }
    }

    private SecretKey getSigningKey() {
        return Keys.secretKeyFor(SignatureAlgorithm.HS256);  // 안전한 256비트 이상의 키 생성
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


        String token = Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)  // 최신 방식의 signWith 사용
                .compact();

        String refreshToken = Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION_TIME))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)  // 최신 방식의 signWith 사용
                .compact();

        jwtTokenService.saveToken(
                token,
                refreshToken,
                subject,
                LocalDateTime.now(),
                LocalDateTime.now().plusSeconds(JWT_EXPIRATION / 1000),
                LocalDateTime.now().plusSeconds(REFRESH_TOKEN_EXPIRATION_TIME / 1000)
        );


        return token;
    }

    // 토큰 검증
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        Optional<JwtToken> storedTokenOpt = jwtTokenService.findByToken(token);

        if (storedTokenOpt.isEmpty()) {
            return false;
        }

        JwtToken storedToken = storedTokenOpt.get();
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token) && !isTokenExpiredInDB(storedToken));
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
