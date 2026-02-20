package com.project.shiphub.security;

import com.project.shiphub.model.auth.User;
import com.project.shiphub.repository.auth.LoginRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class Auth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final LoginRepository loginRepository;
    private final JwtService jwtService;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException {

        log.info("🎉 Login com Google bem-sucedido!");

        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
        OAuth2User oAuth2User = oauthToken.getPrincipal();

        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        String picture = oAuth2User.getAttribute("picture");
        String googleId = oAuth2User.getAttribute("sub");

        log.info("📧 Email: {}", email);
        log.info("👤 Nome: {}", name);
        log.info("🆔 Google ID: {}", googleId);

        User user = loginRepository.findByEmail(email)
                .orElseGet(() -> {
                    log.info("🆕 Criando novo usuário via Google");
                    User newUser = User.builder()
                            .name(name)
                            .email(email)
                            .password(null)
                            .googleId(googleId)
                            .avatarUrl(picture)
                            .oauthProvider("GOOGLE")
                            .build();
                    return loginRepository.save(newUser);
                });

        if (user.getGoogleId() == null) {
            log.info("🔄 Atualizando dados do Google para usuário existente");
            user.setGoogleId(googleId);
            user.setAvatarUrl(picture);
            user.setOauthProvider("GOOGLE");
            loginRepository.save(user);
        }

        String token = jwtService.generateToken(user);
        log.info("🔑 Token JWT gerado");

        addTokenCookie(token, response);

        String redirectUrl = "http://localhost:5173/home?login=success";
        log.info("🔀 Redirecionando para: {}", redirectUrl);

        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }

    private void addTokenCookie(String token, HttpServletResponse response) {
        String cookieValue = String.format(
                "token=%s; Path=/; Max-Age=%d; HttpOnly; SameSite=Lax",
                token,
                60 * 60 * 24 // 24 horas
        );
        response.setHeader("Set-Cookie", cookieValue);
        log.info("🍪 Cookie adicionado");
    }
}