package com.busanit501.testdbmaria.controller;

import com.busanit501.testdbmaria.service.JwtTokenService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Log4j2
@Controller
public class LoginController {
    @GetMapping("/login")
    public String login() {
        return "login"; // login.html 템플릿을 반환
    }

    @GetMapping("/page1")
    public String page1() {
        return "page1"; // login.html 템플릿을 반환
    }

    @GetMapping("/page2")
    public String page2() {
        return "page2"; // login.html 템플릿을 반환
    }
    @GetMapping("/index")
    public String index() {
        return "index"; // login.html 템플릿을 반환
    }

    @Autowired
    private JwtTokenService jwtTokenService;

    @PostMapping("/logout")
    public String logout(HttpServletRequest request) {
        // Authorization 헤더에서 토큰을 가져옵니다.
        String authorizationHeader = request.getHeader("Authorization");
        String token = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7); // "Bearer " 이후의 토큰 문자열을 가져옴
        }

        if (token != null) {
            // 토큰에서 사용자 이름을 추출합니다.
            jwtTokenService.deleteToken(token);
        }

        // 로그아웃 후 로그인 페이지로 리디렉션
        return "redirect:/login?logout";
    }
    @PostMapping("/page1")
    @ResponseBody
    public ResponseEntity<String> postToPage1(@RequestBody Map<String, String> requestBody) {
        // Check if the "name" key exists and equals "admin"
        if ("John".equals(requestBody.get("name"))) {
            log.info("success");
            return ResponseEntity.ok("success");
        } else {
            return ResponseEntity.status(400).body("failure");
        }
    }
}
