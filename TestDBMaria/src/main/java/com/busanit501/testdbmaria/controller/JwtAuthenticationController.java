package com.busanit501.testdbmaria.controller;

import com.busanit501.testdbmaria.entity.JwtToken;
import com.busanit501.testdbmaria.model.AuthenticationRequest;
import com.busanit501.testdbmaria.model.AuthenticationResponse;
import com.busanit501.testdbmaria.secureity.JwtTokenUtil;
import com.busanit501.testdbmaria.service.JwtTokenService;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Lazy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Log4j2
@RestController
@RequestMapping("/api")
public class JwtAuthenticationController {

    @Autowired
    @Lazy
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtTokenService jwtTokenService;


    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        try {
            log.info("Authentication attempt for user: {}", authenticationRequest.getName());
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getName(), authenticationRequest.getPassword())
            );
            log.info("Authentication successful for user: {}", authenticationRequest.getName());
        } catch (BadCredentialsException e) {
            log.error("Authentication failed for user: {}", authenticationRequest.getName(), e);
            throw new Exception("Invalid credentials", e);
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getName());
        final String jwt = jwtTokenUtil.generateToken(userDetails);
        final String refreshToken = jwtTokenUtil.generateRefreshToken(userDetails.getUsername());

        // Get current time for issuedAt and calculate expiration times
        LocalDateTime issuedAt = LocalDateTime.now();
        LocalDateTime expiresAt = issuedAt.plusSeconds(jwtTokenUtil.JWT_EXPIRATION / 1000); // convert milliseconds to seconds
        LocalDateTime refreshExpiresAt = issuedAt.plusSeconds(jwtTokenUtil.REFRESH_TOKEN_EXPIRATION_TIME / 1000);

        // Save or update the token using the service's saveToken method
        jwtTokenService.saveToken(jwt, refreshToken, userDetails.getUsername(), issuedAt, expiresAt, refreshExpiresAt);

        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", jwt);
        tokens.put("refreshToken", refreshToken);

        return ResponseEntity.ok(new AuthenticationResponse(jwt, refreshToken));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestParam String refreshToken) {
        try {
            String username = jwtTokenUtil.extractUsername(refreshToken);

            if (jwtTokenUtil.isTokenExpired(refreshToken)) {
                return ResponseEntity.status(401).body("Refresh token is expired");
            }

            Optional<JwtToken> storedToken = jwtTokenService.findByToken(refreshToken);
            if (storedToken.isEmpty()) {
                return ResponseEntity.status(401).body("Invalid refresh token");
            }

            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            String newAccessToken = jwtTokenUtil.generateToken(userDetails);

            // Update the access token and refresh token expiration times in the database
            LocalDateTime issuedAt = LocalDateTime.now();
            LocalDateTime expiresAt = issuedAt.plusSeconds(jwtTokenUtil.JWT_EXPIRATION / 1000);

            jwtTokenService.saveToken(newAccessToken, refreshToken, userDetails.getUsername(), issuedAt, expiresAt, storedToken.get().getRefreshExpiresAt());

            Map<String, String> tokens = new HashMap<>();
            tokens.put("accessToken", newAccessToken);

            return ResponseEntity.ok(tokens);
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Invalid refresh token");
        }
    }
}
