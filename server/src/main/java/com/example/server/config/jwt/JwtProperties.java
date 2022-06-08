package com.example.server.config.jwt;

public interface JwtProperties {
    String SECRET = "JWT_TOKEN"; // 서버만 아는 비밀 키
    int ACCESS_EXPIRATION_TIME = 15000; // 10분 600000
    int REFRESH_EXPIRATION_TIME = 25000; // 30분 18000000
    String TOKEN_PREFIX = "Bearer ";
    String HEADER_STRING = "Authorization";
    String REFRESH_HEADER_STRING = "Refresh_token";
}

