package com.project.shiphub.security;

import com.project.shiphub.model.auth.User;
import com.project.shiphub.repository.auth.LoginRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final LoginRepository loginRepository;

    @Value("${app.jwtsecret}")
    private String SECRET;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        // ← ADICIONAR LOGS
        System.out.println("JwtFilter - URL: " + request.getRequestURI());
        System.out.println("JwtFilter - Method: " + request.getMethod());

        // Listar todos os cookies
        if (request.getCookies() != null) {
            System.out.println("Cookies recebidos:");
            for (Cookie cookie : request.getCookies()) {
                System.out.println("   - " + cookie.getName() + " = " + cookie.getValue());
            }
        } else {
            System.out.println("Nenhum cookie recebido");
        }

        String token = extractTokenFromCookie(request);

        if (token != null) {
            System.out.println("Token encontrado: " + token.substring(0, Math.min(20, token.length())) + "...");
            try {
                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(Keys.hmacShaKeyFor(SECRET.getBytes()))
                        .build()
                        .parseClaimsJws(token)
                        .getBody();

                String email = claims.getSubject();
                System.out.println("Email do token: " + email);

                User user = loginRepository.findByEmail(email).orElse(null);

                if (user != null) {
                    System.out.println("Usuário autenticado: " + user.getEmail());
                    UsernamePasswordAuthenticationToken auth =
                            new UsernamePasswordAuthenticationToken(
                                    user,
                                    null,
                                    Collections.emptyList()
                            );
                    SecurityContextHolder.getContext().setAuthentication(auth);
                } else {
                    System.out.println("Usuário não encontrado no banco: " + email);
                }
            } catch (Exception e) {
                System.out.println("Erro ao validar token: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("Token não encontrado nos cookies");
        }

        filterChain.doFilter(request, response);
    }

    private String extractTokenFromCookie(HttpServletRequest request) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("token".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}