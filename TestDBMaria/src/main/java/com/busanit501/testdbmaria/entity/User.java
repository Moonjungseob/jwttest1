package com.busanit501.testdbmaria.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.*;
import jakarta.persistence.Id;

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

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }


}