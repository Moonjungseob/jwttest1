package com.busanit501.testdbmaria.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDateTime;


@Entity
@Data
public class JwtToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    private String refreshToken;

    private String name;


    private LocalDateTime issuedAt;

    private LocalDateTime expiresAt;

    private LocalDateTime refreshExpiresAt;
}
