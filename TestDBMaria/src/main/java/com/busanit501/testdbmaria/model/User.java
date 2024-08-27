package com.busanit501.testdbmaria.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "users")
public class User {

    @Id
    private String id;  // MongoDB에서는 기본적으로 ID가 String 타입입니다.

    private String name;
    private String email;
    private String password;
    private int role;

    // 기본 생성자
    public User() {
    }

    // 전체 필드를 사용하는 생성자
    public User(String id, String name, String email, String password, int role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }
}