package com.aurevia.ecommerce.dto;

public class LoginResponse {

    private String message;
    private String token;
    private UserResponse user;

    public LoginResponse(String message, String token, UserResponse user) {
        this.message = message;
        this.token = token;
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public String getToken() {
        return token;
    }

    public UserResponse getUser() {
        return user;
    }
}