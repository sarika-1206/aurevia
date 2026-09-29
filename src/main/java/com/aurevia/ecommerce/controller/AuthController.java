package com.aurevia.ecommerce.controller;

import com.aurevia.ecommerce.dto.LoginRequest;
import com.aurevia.ecommerce.dto.LoginResponse;
import com.aurevia.ecommerce.dto.RegisterRequest;
import com.aurevia.ecommerce.dto.UserResponse;
import com.aurevia.ecommerce.entity.User;
import com.aurevia.ecommerce.security.JwtService;
import com.aurevia.ecommerce.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;

    public AuthController(AuthService authService,
                          JwtService jwtService) {
        this.authService = authService;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(
            @RequestBody RegisterRequest request) {

        User user = authService.register(
                request.getName(),
                request.getEmail(),
                request.getPassword()
        );

        UserResponse response = new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @RequestBody LoginRequest request) {

        User user = authService.login(
                request.getEmail(),
                request.getPassword()
        );

        String token = jwtService.generateToken(user.getEmail());

        UserResponse userResponse = new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole()
        );

        LoginResponse response = new LoginResponse(
                "Login successful",
                token,
                userResponse
        );

        return ResponseEntity.ok(response);
    }
}