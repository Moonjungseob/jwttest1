package com.busanit501.testdbmaria.service;

import com.busanit501.testdbmaria.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import java.util.Arrays;
import java.util.List;

public class DUserService {
    private final RestTemplate restTemplate;

    public DUserService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<User> getAllUsers() {
        String url = "http://localhost:8000/api/users/"; // Django 서버의 API URL

        ResponseEntity<User[]> response = restTemplate.getForEntity(url, User[].class);

        return Arrays.asList(response.getBody());
    }
}