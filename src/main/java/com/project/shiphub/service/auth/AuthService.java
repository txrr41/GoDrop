package com.project.shiphub.service.auth;

import com.project.shiphub.dto.auth.AuthResponse;
import com.project.shiphub.dto.auth.LoginRequest;
import com.project.shiphub.dto.auth.RegisterRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {
    AuthResponse login(LoginRequest request, HttpServletResponse response);
    AuthResponse register(RegisterRequest request, HttpServletResponse response);
    void clearCookie(HttpServletResponse response);
}
