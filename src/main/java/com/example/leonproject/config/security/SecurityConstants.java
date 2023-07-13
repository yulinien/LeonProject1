package com.example.leonproject.config.security;

public class SecurityConstants {
    public static final long JWT_EXPIRATION = 3000000;
    public static final String JWT_SECRET = System.getenv("jwt_key");
}

