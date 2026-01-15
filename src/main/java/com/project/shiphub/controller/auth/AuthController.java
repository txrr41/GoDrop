package com.project.shiphub.controller.auth;

import com.project.shiphub.dto.auth.LoginRequest;
import com.project.shiphub.dto.auth.LoginResponse;
import com.project.shiphub.dto.auth.RegisterRequest;
import com.project.shiphub.dto.auth.RegisterResponse;
import com.project.shiphub.service.auth.AuthService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;


@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        try {
            LoginResponse response = authService.autenticar(loginRequest);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            LoginResponse resp = new LoginResponse();
            resp.setMensagem("Error");
            LoginResponse errorResponse = resp;
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest request) {
        try {
            RegisterResponse response = authService.cadastrar(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (Exception e) {
            RegisterResponse error = new RegisterResponse();
            error.setMensagem(e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

}
