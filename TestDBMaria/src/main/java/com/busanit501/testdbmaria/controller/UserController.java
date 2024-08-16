package com.busanit501.testdbmaria.controller;

import com.busanit501.testdbmaria.entity.User;
import com.busanit501.testdbmaria.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // 사용자 등록 (비밀번호 암호화)
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User createdUser = userService.saveUser(user);
        return ResponseEntity.ok(createdUser);
    }

    // 모든 사용자 조회 (인증 필요)
    @PreAuthorize("hasRole('ADMIN')")  // ADMIN 역할만 접근 가능
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    // 특정 사용자 조회 (인증 필요)
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")  // USER와 ADMIN 역할 모두 접근 가능
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();  // 404 Not Found
        }
    }

    // 특정 사용자 삭제 (ADMIN만 가능)
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();  // 204 No Content
    }
}
