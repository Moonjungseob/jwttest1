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
        final String method = request.getMethod();
        log.info("Processing request for URI: " + requestURI + " with method: " + method);

        // 특정 경로에 대한 필터링 제외 처리 및 모든 GET 요청 제외
        if (method.equalsIgnoreCase("GET") ||
                requestURI.startsWith("/login") ||
                requestURI.startsWith("/api/authenticate") ||
                requestURI.startsWith("/api/mongoUsers") ||
                requestURI.startsWith("/static/") ||
                requestURI.startsWith("/index") ||
                requestURI.startsWith("/classify") ||
                requestURI.startsWith("/api/users")) {
            log.info("Skipping JWT filter for URI: " + requestURI + " with method: " + method);
            chain.doFilter(request, response);
            return;
        }

        final String authorizationHeader = request.getHeader("Authorization");
        log.info("Authorization Header: " + authorizationHeader);

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String jwt = authorizationHeader.substring(7).trim();
            String username = jwtTokenUtil.extractUsername(jwt);
            log.info("Extracted JWT: " + jwt + ", Username: " + username);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                if (jwtTokenUtil.validateToken(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    log.info("User " + username + " is authenticated and added to SecurityContextHolder.");
                }
            }
        } else {
            log.warn("Authorization header is missing or does not start with Bearer.");
        }

        log.info("Continuing filter chain after JWT validation.");
//        chain.doFilter(request, response);
    }
}