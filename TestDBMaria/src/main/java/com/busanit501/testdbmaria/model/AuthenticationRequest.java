package com.busanit501.testdbmaria.model;

import lombok.Data;

@Data
public class AuthenticationRequest {
    private String email;
    private String password;


    public AuthenticationRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
}