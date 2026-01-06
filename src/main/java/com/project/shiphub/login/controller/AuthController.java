package com.project.shiphub.login.controller;

import com.project.shiphub.login.dto.LoginRequest;
import com.project.shiphub.login.dto.LoginResponse;
import com.project.shiphub.login.service.AuthService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;


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

}
