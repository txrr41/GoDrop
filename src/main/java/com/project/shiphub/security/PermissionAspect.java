package com.project.shiphub.security;

import com.project.shiphub.model.auth.AdminProfile;
import com.project.shiphub.model.auth.User;
import com.project.shiphub.model.auth.UserRole;
import com.project.shiphub.repository.auth.AdminProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class PermissionAspect {

    private final AdminProfileRepository adminProfileRepository;

    @Before("@annotation(requiresPermission)")
    public void checkPermission(RequiresPermission requiresPermission) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Não autenticado");
        }

        User user = (User) auth.getPrincipal();

        if (user.getRole() == UserRole.OWNER) return;

        if (user.getRole() == UserRole.CUSTOMER) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Acesso negado");
        }

        String requiredPerm = requiresPermission.value();
        AdminProfile profile = adminProfileRepository.findByUserId(user.getId()).orElse(null);

        if (profile == null || profile.getSuspended()) {
            log.warn("⚠️ Acesso negado para {} — sem perfil ou suspenso", user.getEmail());
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Acesso negado");
        }

        if (!profile.hasPermission(requiredPerm)) {
            log.warn("⚠️ {} não tem permissão '{}'", user.getEmail(), requiredPerm);
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Você não tem permissão para acessar este recurso"
            );
        }

        log.debug("✅ {} autorizado para '{}'", user.getEmail(), requiredPerm);
    }
}