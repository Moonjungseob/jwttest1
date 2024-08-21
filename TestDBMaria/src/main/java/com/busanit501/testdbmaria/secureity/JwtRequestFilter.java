package com.busanit501.testdbmaria.secureity;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Log4j2
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException, IOException {

        final String requestURI = request.getRequestURI();
        // 특정 경로에 대한 필터링 제외 처리
        if (requestURI.startsWith("/login") || requestURI.startsWith("/api/authenticate") || requestURI.startsWith("/static/")|| requestURI.startsWith("/index") ) {
            // 로그인 페이지, 인증 API, 정적 리소스는 필터링하지 않음
            chain.doFilter(request, response);
            return;
        }

        final String authorizationHeader = request.getHeader("Authorization");

        String username = null;
        String jwt = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7).trim(); // 공백 제거
            username = jwtTokenUtil.extractUsername(jwt);
            log.info("유저네임보기" + username);
        } else {
            log.warn("Authorization header is missing or does not start with Bearer.");
            log.info("유저네임보기" + username);
        }

        if (username != null && jwt != null && jwtTokenUtil.validateToken(jwt, userDetailsService.loadUserByUsername(username))) {
            // 토큰이 유효하면 해당 사용자에 대한 인증 작업을 진행합니다.
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (jwtTokenUtil.validateToken(jwt, userDetails)) {
                // 여기에 SecurityContextHolder에 인증 객체를 설정하는 로직을 추가
            }
        }
        chain.doFilter(request, response);
    }
}
