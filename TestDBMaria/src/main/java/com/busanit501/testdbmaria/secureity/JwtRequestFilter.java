package com.busanit501.testdbmaria.secureity;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
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
            throws ServletException, IOException {

        final String requestURI = request.getRequestURI();
        // 특정 경로에 대한 필터링 제외 처리
        if (requestURI.startsWith("/login") || requestURI.startsWith("/api/authenticate") || requestURI.startsWith("/static/")|| requestURI.startsWith("/index") ) {
            // 로그인 페이지, 인증 API, 정적 리소스는 필터링하지 않음
            chain.doFilter(request, response);
            return;
        }

        final String authorizationHeader = request.getHeader("Authorization");
        log.info("Authorization Header: " + authorizationHeader);

        String username = null;
        String jwt = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7).trim(); // 공백 제거
            username = jwtTokenUtil.extractUsername(jwt);
            log.info("Extracted JWT: " + jwt);
            log.info("유저네임보기" + username);
        } else {
            log.warn("Authorization header is missing or does not start with Bearer.");
        }

        if (username != null && jwt != null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            log.info("유저네임보기2" + userDetails);
            boolean isTokenValid = jwtTokenUtil.validateToken(jwt, userDetails);
            log.info("Is Token Valid: " + isTokenValid);

            if (isTokenValid) {
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.info("User " + username + " is authenticated and added to SecurityContextHolder.");
            } else {
                log.warn("JWT token is not valid.");
            }
        }
        chain.doFilter(request, response);
    }
}
