package com.busanit501.testdbmaria.service;


import com.busanit501.testdbmaria.model.User;
import com.busanit501.testdbmaria.repository.MongoUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MongoUserService {
    @Autowired
    private MongoUserRepository mongoUserRepository;

    public User saveUser(User user) {
        return mongoUserRepository.save(user);
    }

    public List<User> getAllUsers() {
        return mongoUserRepository.findAll();
    }

    public Optional<User> getUserById(String id) {
        return mongoUserRepository.findById(id);
    }

    public void deleteUserById(String id) {
        mongoUserRepository.deleteById(id);
    }
}