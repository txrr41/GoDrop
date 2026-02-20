package com.project.shiphub.controller.admin;

import com.project.shiphub.dto.admin.AdminUserDTO;
import com.project.shiphub.dto.admin.AdminUserRequest;
import com.project.shiphub.model.auth.AdminProfile;
import com.project.shiphub.model.auth.User;
import com.project.shiphub.model.auth.UserRole;
import com.project.shiphub.repository.auth.AdminProfileRepository;
import com.project.shiphub.repository.auth.LoginRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
@Slf4j
public class AdminUserController {

    private final LoginRepository loginRepository;
    private final AdminProfileRepository adminProfileRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping
    public ResponseEntity<List<AdminUserDTO>> listAdminUsers(Authentication authentication) {
        assertOwner(authentication);

        List<User> staffAndOwners = loginRepository.findAll().stream()
                .filter(u -> u.getRole() != UserRole.CUSTOMER)
                .collect(Collectors.toList());

        List<AdminUserDTO> result = staffAndOwners.stream()
                .map(u -> {
                    AdminProfile profile = adminProfileRepository.findByUserId(u.getId()).orElse(null);
                    return new AdminUserDTO(u, profile);
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }

    @PostMapping
    public ResponseEntity<AdminUserDTO> createAdminUser(
            @RequestBody AdminUserRequest request,
            Authentication authentication
    ) {
        assertOwner(authentication);

        if (loginRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.badRequest().build();
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword() != null ? request.getPassword() : "Temp@1234"))
                .role(UserRole.STAFF)
                .build();
        loginRepository.save(user);

        AdminProfile profile = AdminProfile.builder()
                .user(user)
                .suspended(false)
                .build();
        profile.setPermissionList(request.getPermissions());
        adminProfileRepository.save(profile);

        log.info("✅ Usuário staff criado: {}", user.getEmail());
        return ResponseEntity.ok(new AdminUserDTO(user, profile));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AdminUserDTO> updateAdminUser(
            @PathVariable Long id,
            @RequestBody AdminUserRequest request,
            Authentication authentication
    ) {
        assertOwner(authentication);

        User user = loginRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        loginRepository.save(user);

        AdminProfile profile = adminProfileRepository.findByUserId(id)
                .orElse(AdminProfile.builder().user(user).build());
        profile.setPermissionList(request.getPermissions());
        adminProfileRepository.save(profile);

        log.info("✅ Permissões atualizadas para: {}", user.getEmail());
        return ResponseEntity.ok(new AdminUserDTO(user, profile));
    }

    @PatchMapping("/{id}/suspend")
    public ResponseEntity<AdminUserDTO> toggleSuspension(
            @PathVariable Long id,
            @RequestBody Map<String, String> body,
            Authentication authentication
    ) {
        assertOwner(authentication);

        User user = loginRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        AdminProfile profile = adminProfileRepository.findByUserId(id)
                .orElse(AdminProfile.builder().user(user).build());

        boolean suspend = Boolean.parseBoolean(body.getOrDefault("suspend", "true"));
        profile.setSuspended(suspend);
        profile.setSuspensionReason(body.get("reason"));
        adminProfileRepository.save(profile);

        log.info("🚫 Usuário {} {}", user.getEmail(), suspend ? "SUSPENSO" : "REATIVADO");
        return ResponseEntity.ok(new AdminUserDTO(user, profile));
    }

    private void assertOwner(Authentication authentication) {
        if (authentication == null) throw new RuntimeException("Não autenticado");
        User requester = (User) authentication.getPrincipal();
        if (requester.getRole() != UserRole.OWNER) {
            throw new RuntimeException("Acesso negado: apenas o dono pode gerenciar usuários");
        }
    }
}