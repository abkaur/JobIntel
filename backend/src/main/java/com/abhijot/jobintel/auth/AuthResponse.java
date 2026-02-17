package com.abhijot.jobintel.auth;

public record AuthResponse(
        Long id,
        String name,
        String email,
        String role
) {}

