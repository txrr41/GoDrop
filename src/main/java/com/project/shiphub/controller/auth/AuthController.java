package com.project.shiphub.controller.auth;

import com.project.shiphub.dto.auth.AuthResponse;
import com.project.shiphub.dto.auth.LoginRequest;
import com.project.shiphub.dto.auth.RegisterRequest;
import com.project.shiphub.model.auth.User;
import com.project.shiphub.service.auth.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @RequestBody LoginRequest request,
            HttpServletResponse response
    ) {
        AuthResponse auth = authService.login(request, response);
        return ResponseEntity.ok(auth);
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(
            @RequestBody RegisterRequest request,
            HttpServletResponse response
    ) {
        AuthResponse auth = authService.register(request, response);
        return ResponseEntity.ok(auth);
    }

    @GetMapping("/me")
    public AuthResponse me(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return new AuthResponse(user);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        authService.clearCookie(response);
        return ResponseEntity.ok().build();
    }
}
