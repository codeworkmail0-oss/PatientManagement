package com.xyfer.AuthService.Controller;

import com.xyfer.AuthService.Dto.LoginRequest;
import com.xyfer.AuthService.Dto.LoginResponse;
import com.xyfer.AuthService.Dto.RegisterRequest;
import com.xyfer.AuthService.Service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Register a new user and return token")
    @PostMapping("/register")
    public ResponseEntity<LoginResponse> register(
            @RequestBody @Valid RegisterRequest registerRequest) {

        return authService.register(registerRequest)
                .map(token -> ResponseEntity.status(HttpStatus.CREATED).body(new LoginResponse(token)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.CONFLICT).build()); // 409 - email taken
    }

    @Operation(summary = "Generate token on user login")
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @RequestBody @Valid LoginRequest loginRequest) {

        Optional<String> tokenOptional = authService.authenticate(loginRequest);

        return tokenOptional.map(s -> ResponseEntity.ok(new LoginResponse(s))).orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());

    }

    @Operation(summary = "Validate Token")
    @GetMapping("/validate")
    public ResponseEntity<Void> validateToken(
            @RequestHeader("Authorization") String authHeader) {


        if (!authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return authService.validateToken(authHeader.substring(7))
                ? ResponseEntity.ok().build()
                : ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }




    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<Void> handleMissingHeader(MissingRequestHeaderException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}