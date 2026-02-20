package com.project.shiphub.service.auth;

import com.project.shiphub.dto.auth.AuthResponse;
import com.project.shiphub.dto.auth.LoginRequest;
import com.project.shiphub.dto.auth.RegisterRequest;
import com.project.shiphub.model.auth.AdminProfile;
import com.project.shiphub.model.auth.User;
import com.project.shiphub.model.auth.UserRole;
import com.project.shiphub.repository.auth.AdminProfileRepository;
import com.project.shiphub.repository.auth.LoginRepository;
import com.project.shiphub.security.JwtService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ServiceImp implements AuthService {

    private final LoginRepository loginRepository;
    private final AdminProfileRepository adminProfileRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    public AuthResponse login(LoginRequest request, HttpServletResponse response) {
        User user = loginRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Senha incorreta");
        }

        if (user.getRole() == UserRole.STAFF) {
            Optional<AdminProfile> profileOpt = adminProfileRepository.findByUserId(user.getId());
            if (profileOpt.isPresent() && profileOpt.get().getSuspended()) {
                throw new RuntimeException("Sua conta está suspensa. Entre em contato com o administrador.");
            }
        }

        String token = jwtService.generateToken(user);
        addTokenCookie(token, response);

        return buildAuthResponse(user);
    }

    @Override
    public AuthResponse register(RegisterRequest request, HttpServletResponse response) {
        if (loginRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("E-mail já cadastrado");
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(UserRole.CUSTOMER)
                .build();

        loginRepository.save(user);

        String token = jwtService.generateToken(user);
        addTokenCookie(token, response);

        return buildAuthResponse(user);
    }

    @Override
    public void clearCookie(HttpServletResponse response) {
        jakarta.servlet.http.Cookie cookie = new jakarta.servlet.http.Cookie("token", null);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

    public AuthResponse buildAuthResponse(User user) {
        if (user.getRole() == UserRole.CUSTOMER) {
            return new AuthResponse(user);
        }


        AdminProfile adminProfile = adminProfileRepository
                .findByUserId(user.getId())
                .orElse(null);

        return new AuthResponse(user, adminProfile);
    }

    private void addTokenCookie(String token, HttpServletResponse response) {
        String cookieValue = String.format(
                "token=%s; Path=/; Max-Age=%d; HttpOnly; SameSite=Lax",
                token,
                60 * 60 * 24
        );
        response.setHeader("Set-Cookie", cookieValue);
        System.out.println("🍪 Cookie configurado");
    }
}