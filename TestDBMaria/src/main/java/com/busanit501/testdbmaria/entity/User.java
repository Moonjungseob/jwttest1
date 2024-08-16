package com.busanit501.testdbmaria.entity;

import jakarta.persistence.*;
import lombok.*;


@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String password;  // 로그인 시 사용할 비밀번호 필드

    @Column(nullable = false)
    private int role; // 역할 (0: 일반 사용자, 1: 관리자)

    public User(String name, String email, String password, int role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public String getRoleAsString() {
        return this.role == 1 ? "ROLE_ADMIN" : "ROLE_USER";
    }
}
