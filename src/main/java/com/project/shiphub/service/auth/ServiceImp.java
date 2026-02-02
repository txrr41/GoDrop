package com.project.shiphub.service.auth;


import com.project.shiphub.dto.auth.AuthResponse;
import com.project.shiphub.dto.auth.LoginRequest;
import com.project.shiphub.dto.auth.RegisterRequest;
import com.project.shiphub.model.auth.User;
import com.project.shiphub.repository.auth.LoginRepository;
import com.project.shiphub.security.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ServiceImp implements AuthService {

    private final LoginRepository loginRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    public AuthResponse login(LoginRequest request, HttpServletResponse response) {

        User user = loginRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Senha incorreta");
        }

        String token = jwtService.generateToken(user);

        addTokenCookie(token, response);

        return new AuthResponse(user);
    }

    @Override
    public AuthResponse register(RegisterRequest request, HttpServletResponse response) {

        if (loginRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("E-mail já cadastrado");
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        loginRepository.save(user);

        String token = jwtService.generateToken(user);

        addTokenCookie(token, response);

        return new AuthResponse(user);
    }

    @Override
    public void clearCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie("token", null);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

    private void addTokenCookie(String token, HttpServletResponse response) {
        String cookieValue = String.format(
                "token=%s; Path=/; Max-Age=%d; HttpOnly; SameSite=Lax",
                token,
                60 * 60 * 24  // 24 horas em segundos
        );

        response.setHeader("Set-Cookie", cookieValue);

        // Debug log
        System.out.println("🍪 Set-Cookie header: " + cookieValue.substring(0, Math.min(60, cookieValue.length())) + "...");
    }
}
