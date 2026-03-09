package com.xyfer.AuthService.Service;

import com.xyfer.AuthService.Dto.LoginRequest;
import com.xyfer.AuthService.Dto.RegisterRequest;
import com.xyfer.AuthService.Model.User;
import com.xyfer.AuthService.Util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public Optional<String> authenticate(LoginRequest loginRequest) {
        return userService
                .findByEmail(loginRequest.getEmail())
                .filter(u -> passwordEncoder.matches(loginRequest.getPassword(), u.getPassword()))
                .map(u -> jwtUtil.generateToken(u.getEmail(), u.getRole()));
    }

    public Optional<String> register(RegisterRequest request) {
        if (userService.findByEmail(request.getEmail()).isPresent()) {
            log.warn("Registration failed — email already in use: {}", request.getEmail());
            return Optional.empty();
        }
        User newUser = new User(
                null,
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                request.getRole() != null ? request.getRole() : "USER"
        );
        userService.save(newUser);
        log.info("New user registered: {}", newUser.getEmail());
        return Optional.of(jwtUtil.generateToken(newUser.getEmail(), newUser.getRole()));
    }

    public boolean validateToken(String token) {
        try {
            jwtUtil.validateToken(token);
            return true;
        } catch (Exception e) {
            log.warn("Token validation failed: {}", e.getMessage());
            return false;
        }
    }
}